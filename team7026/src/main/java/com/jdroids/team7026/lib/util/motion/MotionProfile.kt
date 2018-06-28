package com.jdroids.team7026.lib.util.motion

import android.util.Log
import com.jdroids.team7026.lib.util.Util

/**
 * A motion profile specifies a 1D time-parametrized trajectory. The trajectory is composed of
 * successively coincident MotionSegments from which the desired state of motion at any given time
 * or distance can be calculated
 */
class MotionProfile {
    val segments: ArrayList<MotionSegment>

    /**
     * Construct an empty motion profile
     */
    constructor() {
        this.segments = ArrayList()
    }

    /**
     * Creates a MotionProfile from existing lists of segments (note that validity is not checked)
     *
     * @param segments
     *      The new segments of the profile
     */
    constructor(segments: ArrayList<MotionSegment>) {
        this.segments = segments
    }

    /**
     * Checks that:
     * 1. All segments are valid
     *
     * 2. Successive segments are C1 continuous in position and C0 continuous in velocity
     *
     * @return The validity of the MotionProfile
     */
    fun isValid(): Boolean {
        var previousSegment: MotionSegment? = null

        for (s: MotionSegment in this.segments) {
            if (!s.isValid()) {
                return false
            }
            if (previousSegment != null && !s.start.coincident(previousSegment.end)) {
                //Adjacent segments are not continuous
                Log.e("MotionProfile", """Segments not continuous! End:
                      ${previousSegment.end}, Start: ${s.start}""")
                return false
            }
            previousSegment = s
        }

        return true
    }

    /**
     * Check if the profile is empty
     *
     * @return True if there are no segments
     */
    fun isEmpty(): Boolean = this.segments.isEmpty()

    /**
     * Get the interpolated MotionState at any time
     *
     * @param t
     *      The time to query
     *
     * @return null if the resulting MotionState is outside the time bounds of the profile,
     * or the resulting MotionState otherwise
     */
    fun stateByTime(t: Double): MotionState? {
        if (t < startTime() && t + Util.epsilon >= startTime()) {
            return startState()
        }
        if (t > endTime() && t - Util.epsilon <= endTime()) {
            return endState()
        }
        for (segment in this.segments) {
            if (segment.containsTime(t)) {
                return segment.start.extrapolate(t)
            }
        }
        return null
    }

    /**
     * Get the interpolated MotionState at any given time, clamping to the end points if out of
     * bounds
     *
     * @param t
     *      The time to query.
     * @return The MotionState at time t, or closest to it if t is outside of the profile.
     */
    fun stateByTimeClamped(t: Double): MotionState {
        if (t < startTime()) {
            return startState()
        }
        else if (t > endTime()) {
            return endState()
        }

        for (segment in this.segments) {
            if (segment.containsTime(t)) {
                return segment.start.extrapolate(t)
            }
        }
        //Should never get here
        return MotionState.invalidState
    }

    /**
     * Get the interpolated MotionState by distance (the pos field of MotionState). Note that since
     * a profile may reverse, this only returns the *first* instance of this position
     *
     * @param pos
     *      The position to query.
     *
     * @return null if the profile never crosses pos or if the profile is invalid, or the resulting
     *      MotionState otherwise.
     */
    fun firstStateByPos(pos: Double): MotionState? {
        for (segment in this.segments) {
            if (segment.containsPos(pos)) {
                if (Util.epsilonEquals(segment.end.pos, pos)) {
                    return segment.end
                }
                val t = Math.min(segment.start.nextTimeAtPos(pos), segment.end.t)
                if (t == Double.NaN) {
                    Log.e("MotionProfile", "Error! We should reach 'pos' but don't")
                    return null
                }
                return segment.start.extrapolate(t)
            }
        }
        //We never reach pos
        return null
    }

    /**
     * Remove all parts of the profile prior to query time. This eliminates whole segments and
     * shortens segments containing t.
     *
     * @param t
     *      The query time.
     */
    fun trimBeforeTime(t: Double) {
        for (segment in this.segments) {
            if (segment.end.t <= t) {
                //Segment is fully before t
                this.segments.remove(segment)
            }
            else if (segment.start.t <= t) {
                //Segment is partially before t; let's shorten the segment
                segment.start = segment.start.extrapolate(t)
            }
        }
    }

    /**
     * Removes all segments.
     */
    fun clear() {
        this.segments.clear()
    }

    /**
     * Remove all segments and initialize to the desired state (actually a segment of length 0 that
     * starts and ends at initial state)
     * @param initialState
     *      The MotionState to initialize to
     */
    fun reset(initialState: MotionState) {
        this.clear()
        this.segments.add(MotionSegment(initialState, initialState))
    }

    /**
     * Remove unnecessary segments (segments whose start and end states are coincident)
     */
    fun consolidate() {
        for (segment in this.segments.iterator()) {
            if (segment.start.coincident(segment.end)) {
                this.segments.remove(segment)
            }
        }
    }

    /**
     * Add to the profile by applying an acceleration control for a given time. This is appended to
     * the last previous state.
     *
     * @param acc
     *      The acceleration to apply.
     * @param dt
     *      The period of time to apply to the given acceleration.
     */
    fun appendControl(acc: Double, dt: Double) {
        if (isEmpty()) {
            Log.e("MotionProfile", "Trying to append to an empty profile")
            return
        }
        val lastEndState = segments[segments.size - 1].end
        val newStartState = MotionState(lastEndState.t, lastEndState.pos, lastEndState.vel, acc)

        appendSegment(MotionSegment(newStartState,
                      newStartState.extrapolate(newStartState.t + dt)))
    }

    /**
     * Append a segment to the profile. No validity checking is done
     *
     * @param segment
     *      The segment to append
     */
    fun appendSegment(segment: MotionSegment) {
        this.segments.add(segment)
    }

    /**
     * Append all the segments of an existing profile to this profile. No validity checking is done
     *
     * @param profile
     *      The profile to append
     */
    fun appendProfile(profile: MotionProfile) {
        for (s in profile.segments) {
            appendSegment(s)
        }
    }

    /**
     * @return The number of segments
     */
    fun size() = segments.size

    /**
     * @return The first state in the profile (or invalidState if empty)
     */
    fun startState(): MotionState {
        if (isEmpty()) {
            return MotionState.invalidState
        }
        return segments[0].start
    }

    /**
     * @return The time of the first state in the profile or NaN if empty
     */
    fun startTime(): Double {
        return startState().t
    }

    /**
     * @return The pos of the first state of the profile or Nan if empty
     */
    fun startPos(): Double {
        return startState().pos
    }


    /**
     * @return The last state in the profile (or invalidState if empty)
     */
    fun endState(): MotionState {
        if (isEmpty()) {
            return MotionState.invalidState
        }
        return segments[segments.size - 1].start
    }

    /**
     * @return The time of the last state in the profile or NaN if empty
     */
    fun endTime(): Double {
        return endState().t
    }

    /**
     * @return The pos of the last state of the profile or Nan if empty
     */
    fun endPos(): Double {
        return startState().pos
    }

    /**
     * @return The total distance the profile covers. Keep in mind distance is the sum of absolute
     *      distances traveled, so a MotionProfile that reverses will count the distance covered
     *      each direction.
     */
    fun length(): Double {
        var length = 0.0
        for (s in this.segments) {
            length += Math.abs(s.end.pos - s.start.pos)
        }

        return length
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for (s in this.segments) {
            sb.append("\n$s")
        }

        return sb.toString()
    }
}