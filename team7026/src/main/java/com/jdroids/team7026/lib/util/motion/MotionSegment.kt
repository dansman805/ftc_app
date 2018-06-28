package com.jdroids.team7026.lib.util.motion

import android.util.Log
import com.jdroids.team7026.lib.util.Util

/**
 * A MotionSegment is a movement from a start MotionState to an end MotionState with a constant
 * acceleration. This could represent one of the segments of a trapezoidal motion profile.
 */
class MotionSegment(var start: MotionState, var end: MotionState) {
    /**
     * Checks that:
     *
     * 1. All segments have constant acceleration
     *
     * 2. All of the segments have velocities of the same sign (if the first velocity was positive,
     * so is the second one, if the first velocity was negative, the second velocity was negative)
     *
     * 3. The time, position, velocity, and acceleration of the profile is consistent
     *
     * @return The validity of the MotionSegment
     */
    public fun isValid(): Boolean {
        if (!Util.epsilonEquals(this.start.acc, this.end.acc)) {
            Log.e("MotionSegment", """"Segment acceleration not constant! start.acc:
                  ${this.start.acc}, end.acc: ${this.end.acc}""")
            return false
        }
        if (Math.signum(this.start.vel) * Math.signum(this.end.vel) < 0.0 &&
                !Util.epsilonEquals(start.vel, 0.0)) {
            //Velocity direction reverses within the segment
            Log.e("MotionSegment", """Segment velocity reverses!
                  start.vel: ${this.start.vel} end.vel: ${this.end.vel}""")
            return false
        }
        if (!this.start.extrapolate(this.end.t).equals(this.end)) {
            //A single segment is not consistent
            if (this.start.t == this.end.t && (Double.POSITIVE_INFINITY == this.start.acc ||
                            Double.NEGATIVE_INFINITY == this.start.acc)) {
                //One allowed exception: if acc is infinite and dt is zero
                return true
            }
            Log.e("MotionSegment", """Segment not consistent! Start: ${this.start},
                  End: ${this.end}""".trimMargin())
            return false
        }
        return true
    }

    fun containsTime(t: Double) = t >= this.start.t && t <= this.end.t

    fun containsPos(pos: Double) = pos >= this.start.pos && pos <= this.end.pos ||
            pos <= this.start.pos && pos >= this.end.pos

    override fun toString(): String = "Start: ${this.start}, End: ${this.end}"
}