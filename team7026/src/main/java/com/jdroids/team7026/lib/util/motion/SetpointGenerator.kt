package com.jdroids.team7026.lib.util.motion

/**
 * A SetpointGenerator does just-in-time motion profile generation to supply a stream of Setpoints
 * that obey the given constraints to the controller. The profile is regenerated when any of the
 * inputs change, but is cached (and trimmed as we go) if the only update is to the current state.
 *
 * Note that typically for smooth control, a user will feed the last iteration's setpoint as the
 * argument to getSetpoint(), and should only use a measured state directly on the first iteration
 * or if a large disturbance is detected.
 */
class SetpointGenerator {
    /**
     * A Setpoint is just a MotionState that adds a flag to indicate if this is the setpoint that
     * achieves the goal. (This is useful for higher-level logic to know that it is now time to do
     * something else)
     */
    class Setpoint(var motionState: MotionState, var finalSetpoint: Boolean) {}

    private var profile: MotionProfile? = null
    private var goal: MotionProfileGoal? = null
    private var motionProfileConstraints: MotionProfileConstraints? = null

    /**
     * Force a reset of the profile.
     */
    fun reset() {
        this.profile = null
        this.goal = null
        this.motionProfileConstraints = null
    }

    /**
     * Get a new Setpoint (and generate a new MotionProfile if needed).
     *
     * @param constraints
     *      The constraints to use.
     * @param goal
     *      The goal to use.
     * @param prevState
     *      The previous Setpoint (or measured state of the system to do a reset)
     * @param t
     *      The time to generate a Setpoint for.
     * @return The new Setpoint at time t.
     */
    fun getSetpoint(constraints: MotionProfileConstraints, goal: MotionProfileGoal,
                    prevState: MotionState, t: Double): Setpoint {
        var regenerate = this.motionProfileConstraints == null ||
                this.motionProfileConstraints!! != constraints || this.goal == null ||
                this.goal != goal || this.profile == null

        if (!regenerate && !this.profile!!.isEmpty()) {
            val expectedState = this.profile!!.stateByTime(t)
            regenerate = expectedState == null || expectedState.equals(prevState)
        }
        if (regenerate) {
            //Regenerate the profile, as the existing profile does not match the inputs
            this.motionProfileConstraints = constraints
            this.goal = goal
            this.profile = MotionProfileGenerator.generateProfile(constraints, goal, prevState)
        }

        //Sample the profile at time t.
        var rv: Setpoint? = null
        if(this.profile != null && this.profile!!.isValid()) {
            val setpoint: MotionState = when {
                t > this.profile!!.endTime() -> this.profile!!.endState()
                t < this.profile!!.startTime() -> this.profile!!.startState()
                else -> this.profile!!.stateByTime(t)!!
            }
            //Shorten the profile and return the new setpoint
            this.profile!!.trimBeforeTime(t)
            rv = Setpoint(setpoint, this.profile!!.isEmpty() ||
                    this.goal!!.atGoalState(setpoint))
        }

        //Invalid or empty profile; just output the same state again
        if (rv == null) {
            rv = Setpoint(prevState, true)
        }

        if (rv.finalSetpoint) {
            //Ensure the final setpoint matches the goal exactly.
            rv.motionState = MotionState(rv.motionState.t, this.goal!!.pos(),
                    Math.signum(rv.motionState.vel) * Math.max(goal.maxAbsVel(),
                            Math.abs(rv.motionState.vel)), 0.0)
        }

        return rv
    }

    /**
     * Get the full profile from the latest call of getSetpoint(). Useful to check estimate time or
     * distance to goal.
     *
     * @return The profile from the latest call to getSetpoint(), or null if there is not yet a
     * profile
     */
    fun getProfile(): MotionProfile? {
        return this.profile
    }
}