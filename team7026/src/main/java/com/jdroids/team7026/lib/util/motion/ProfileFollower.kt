package com.jdroids.team7026.lib.util.motion

/**
 * A controller for following a profile generated to attain a MotionProfileGoal. The controller uses
 * feedforward on acceleration, velocity, and position; proportional feedback on velocity and
 * position; and integral feedback on position
 */
class ProfileFollower {
    private var kp = Double.NaN
    private var ki = Double.NaN
    private var kv = Double.NaN
    private var kffv = Double.NaN
    private var kffa = Double.NaN

    private var minOutput = Double.NEGATIVE_INFINITY
    private var maxOutput = Double.POSITIVE_INFINITY
    private var latestActualState= MotionState.invalidState
    private var initialState = MotionState.invalidState
    private var latestPosError = Double.NaN
    private var latestVelError = Double.NaN
    private var totalError = Double.NaN

    private var goal: MotionProfileGoal? = null
    private var constraints: MotionProfileConstraints? = null
    private var setpointGenerator = SetpointGenerator()
    private var latestSetpoint: SetpointGenerator.Setpoint? = null

    /**
     * Constructs a new ProfileFollower.
     *
     * @param kp
     *      The proportional gain on the position error.
     * @param ki
     *      The integral gain on velocity error.
     * @param kv
     *      The proportional gain on velocity error (or derivative gain on position error).
     * @param kffv
     *      The feedforward gain on velocity. Should be 1.0 if the units of the profile match the
     *      units of the output.
     * @param kffa
     *      The feedforward gain on acceleration.
     */
    constructor(kp: Double, ki: Double, kv: Double, kffv: Double, kffa: Double) {
        resetProfile()
        setGains(kp, ki, kv, kffv, kffa)
    }


    fun setGains(kp: Double, ki: Double, kv: Double, kffv: Double, kffa: Double) {
        this.kp = kp
        this.ki = ki
        this.kv = kv
        this.kffv = kffv
        this.kffa = kffa
    }

    /**
     *  Completely clear all state related to the current profile (min and max outputs are
     *  maintained)
     */
    fun resetProfile() {
        this.totalError = 0.0
        this.initialState = MotionState.invalidState
        this.latestActualState = MotionState.invalidState
        this.latestPosError = Double.NaN
        this.latestVelError = Double.NaN
        this.setpointGenerator.reset()
        this.goal = null
        this.constraints = null
        this.resetSetpoint()
    }

    /**
     * Specify a goal and constraints to reach aforementioned goal.
     */
    fun setGoalAndConstraints(goal: MotionProfileGoal, constraints: MotionProfileConstraints) {
        if (this.goal != null && this.goal!! != goal && this.latestSetpoint != null) {
            //Clear the final state bit since the goal has changed
            this.latestSetpoint!!.finalSetpoint = false
        }
        this.goal = goal
        this.constraints = constraints
    }

}