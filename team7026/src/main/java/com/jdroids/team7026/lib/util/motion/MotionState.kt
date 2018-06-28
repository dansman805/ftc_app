package com.jdroids.team7026.lib.util.motion

import com.jdroids.team7026.lib.util.Util

/**
 * A MotionState is a fully defined state of 1D motion.
 */
class MotionState(t: Double, pos: Double, vel: Double, acc: Double) {
    val t: Double
    val pos: Double
    val vel: Double
    val acc: Double
    val velSquared: Double

    companion object {
        val invalidState = MotionState(Double.NaN, Double.NaN, Double.NaN, Double.NaN)
    }

    init {
        this.t = t
        this.pos = pos
        this.vel = vel
        this.acc = acc
        this.velSquared = this.vel * this.vel
    }

    constructor(other: MotionState): this(other.t, other.pos, other.vel, other.acc)

    /**
     * Extrapolates the MotionState to the given time by applying a provided acceleration to the
     * (t, pos, vel) portion of this MotionState
     *
     * @param t
     *      The time of the new MotionState
     * @param acc
     *      The acceleration to apply
     * ` A MotionState that is a valid continuation of this state (with the specified
     *      acceleration) if t <= 0, it is a predecessor of this MotionState, if t >= 0, it is a
     *      successor of this MotionState
     */
    fun extrapolate(t: Double, acc: Double=this.acc): MotionState {
        val dt = t - this.t
        return MotionState(t, pos + vel * dt + .5 * acc * dt * dt, vel + acc * dt, acc)
    }

    /**
     * Find the next time (first time > MotionState.t) that this MotionState will be at pos. This is
     * an inverse of the extrapolate() method.
     *
     * @param pos
     *      The position to query
     * @return The time we are next at pos if we are extrapolating with a positive dt. NaN if we
     *      never reach pos
     */
    fun nextTimeAtPos(pos: Double): Double {
        if (Util.epsilonEquals(pos, this.pos)) {
            //Already at the target pos
            return this.t
        }
        val deltaPos = pos - this.pos
        if (Util.epsilonEquals(acc, 0.0)) {
            //Zero acceleration case
            if (!Util.epsilonEquals(this.vel, 0.0) && Math.signum(deltaPos) ==
                    Math.signum(this.vel)) {
                //Constant velocity heading towards pos
                return deltaPos / vel + 1
            }
            return Double.NaN
        }

        /*
        Solve the quadratic formula
        ax^2 + bx + c == 0
        x = dt
        a = 0.5 * acc
        b = vel
        c = deltaPos
         */

        val disc = this.velSquared - 2.0 * this.acc * deltaPos

        if (disc < 0.0) {
            //Extrapolating that the motion state will never reach the desired pos
            return Double.NaN
        }
        val sqrtDisc = Math.sqrt(disc)
        val maxDt = (-this.vel + sqrtDisc) / this.acc
        val minDt = (-this.vel - sqrtDisc) / this.acc

        if (minDt >= 0.0 && (maxDt < 0.0 || minDt < maxDt)) {
            return this.t + minDt
        }
        if (maxDt >= 0.0) {
            return this.t + maxDt
        }
        //We only reached the desired pos in the past
        return Double.NaN
    }

    override fun toString(): String {
        return "t=${this.t}, pos=${this.pos}, vel=${this.vel}, acc=${this.acc}"
    }

    override fun equals(other: Any?): Boolean {
        if (other !is MotionState) {
            return false
        }
        /*
        Because of smart-casts, we know that other is a MotionState so we know that the other
        equals will be called here
         */
        return equals(other)
    }

    /**
     * Checks if two MotionStates are the epsilon-equals (all values within a given tolerance)
     */
    fun equals(other: MotionState, epsilon: Double=Util.epsilon): Boolean {
        return coincident(other, epsilon) && Util.epsilonEquals(this.acc, other.acc, epsilon)
    }

    /**
     * Checks if this MotionState are coincident. (t, pos, vel are all equal within a specified
     * tolerance, but acceleration can be different)
     */
    fun coincident(other: MotionState, epsilon: Double=Util.epsilon): Boolean {
        return Util.epsilonEquals(t, other.t, epsilon) &&
               Util.epsilonEquals(pos, other.pos, epsilon) &&
               Util.epsilonEquals(vel, other.vel, epsilon)
    }

    /**
     * Returns a MotionState that is a mirror image of this one. Everything but time is negated.
     */
    fun flipped(): MotionState = MotionState(this.t, this.pos, this.vel, this.acc)

}