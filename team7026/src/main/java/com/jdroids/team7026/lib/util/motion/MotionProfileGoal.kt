package com.jdroids.team7026.lib.util.motion

import com.jdroids.team7026.lib.util.Util

/**
 * A MotionProfileGoal defines the desired position and maximum velocity (at the
 * aforementioned position), along with the behavior that should be used when
 * the robot is at the goal and cannot reach the goal within the desired
 * velocity bounds.
 */

class MotionProfileGoal {
    /**
     * A goal consists of a desired position and a given maximum velocity. There
     * are several options if the goal is reached at a velocity greater than the
     * maximum. This enum allows the user to select a preference of these
     * options.
     *
     * What each does:
     *
     * OVERSHOOT - Usually used with a maxAbsVel of 0.0 to stop at the desired
     * velocity without violating any constraints.
     *
     * VIOLATE_MAX_ACCEL - If we absolutely do not want to pass the goal and are
     * unwilling to violate the max_abs_vel (for example, there is an obstacle
     * in front of us - slam the brakes harder than we'd like in order to avoid
     * hitting it).
     *
     * VIOLATE_MAX_ABS_VEL - If the max velocity is just a general guideline and
     * not a hard performance limit, it's better to slightly exceed it to avoid
     * skidding wheels.
     */
    enum class CompletionBehavior {
        /*
         * Overshoot the goal if necessary (at a velocity greater than
         * maxAbsVel) and come back.
         * Only valid if goal velocity is 0.0, if it isn't VIOLATE_MAX_ACCEL is
         * used.
         */
        OVERSHOOT,
        /*
         * If we cannot slow down to the goal velocity before reaching the goal,
         * allow violating the max accel constraint.
         */
        VIOLATE_MAX_ACCEL,
        /*
         * If we cannot slow down to the goal velocity before crossing the goal,
         * allow exceeding the goal velocity
         */
        VIOLATE_MAX_ABS_VEL
    }

    private var pos = 0.0
    private var maxAbsVel = 0.0
    private var completionBehavior = CompletionBehavior.OVERSHOOT
    private var posTolerance = 1E-3
    private var velTolerance = 1E-2

    constructor() {}

    constructor(pos: Double, maxAbsVel: Double,
                completionBehavior: CompletionBehavior, posTolerance: Double,
                velTolerance: Double) {
        this.pos = pos
        this.maxAbsVel = maxAbsVel
        this.completionBehavior = completionBehavior
        this.posTolerance = posTolerance
        this.velTolerance = velTolerance
        this.sanityCheck()
    }

    constructor(other: MotionProfileGoal) {
        this.pos = other.pos
        this.maxAbsVel = other.maxAbsVel
        this.completionBehavior = other.completionBehavior
        this.posTolerance = other.posTolerance
        this.velTolerance = other.velTolerance
    }

    constructor(pos: Double, maxAbsVel: Double,
                completionBehavior: CompletionBehavior) {
        this.pos = pos
        this.maxAbsVel = maxAbsVel
        this.completionBehavior = completionBehavior
        this.sanityCheck()
    }

    constructor(pos: Double, maxAbsVel: Double) {
        this.pos = pos
        this.maxAbsVel = maxAbsVel
        this.sanityCheck()
    }

    fun pos(): Double {
        return this.pos
    }

    fun maxAbsVel(): Double {
        return this.maxAbsVel
    }

    fun posTolerance(): Double {
        return this.posTolerance
    }

    fun velTolerance(): Double {
        return this.velTolerance
    }

    fun completionBehavior(): CompletionBehavior {
        return this.completionBehavior
    }

    /**
     * @return A flipped MotionProfileGoal (pos is flipped, everything else is
     * the same)
     */
    fun flipped(): MotionProfileGoal {
        return MotionProfileGoal(-pos, maxAbsVel, completionBehavior,
                posTolerance, velTolerance)
    }

    fun atGoalState(state: MotionState): Boolean {
        return this.atGoalPos(state.pos) && Math.abs(state.vel) < (maxAbsVel + velTolerance) ||
                this.completionBehavior == CompletionBehavior.VIOLATE_MAX_ABS_VEL
    }

    fun atGoalPos(pos: Double): Boolean {
        return Util.epsilonEquals(pos, this.pos, posTolerance)
    }

    /**
     * This functions double checks that the provided completion behavior is
     * possible with the max goal velocity
     */
    private fun sanityCheck() {
        if (maxAbsVel > velTolerance && completionBehavior ==
                CompletionBehavior.OVERSHOOT) {
            completionBehavior = CompletionBehavior.VIOLATE_MAX_ACCEL
        }
    }

    override fun toString(): String{
        return """pos: $pos (+/- $posTolerance), maxAbsVel: $maxAbsVel
                (+/- $velTolerance), completion behavior: $completionBehavior"""
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != this.javaClass) return false

        other as MotionProfileGoal

        return (other.completionBehavior() == this.completionBehavior() &&
                other.pos() == this.pos() && other.maxAbsVel() ==
                this.maxAbsVel() && other.posTolerance() == this.posTolerance()
                && other.velTolerance == this.velTolerance())
    }
}