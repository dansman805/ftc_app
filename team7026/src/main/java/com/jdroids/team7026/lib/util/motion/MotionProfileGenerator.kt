package com.jdroids.team7026.lib.util.motion

import com.jdroids.team7026.lib.util.motion.MotionProfileGoal.CompletionBehavior

/**
 * A MotionProfileGenerator generates minimum-time MotionProfiles to travel from a given MotionState
 * to a given MotionProfileGoal while obeying the set of MotionProfileConstraints
 */
object MotionProfileGenerator {
    fun generateFlippedProfile(constraints: MotionProfileConstraints, goalState: MotionProfileGoal,
                               prevState: MotionState): MotionProfile {
        val profile = generateProfile(constraints, goalState.flipped(), prevState.flipped())
        for (segment in profile.segments) {
            segment.start = segment.start.flipped()
            segment.end = segment.end.flipped()
        }
        return profile
    }

    /**
     * Generates a MotionProfile.
     *
     * @param constraints
     *      The constraints to use.
     * @param goalState
     *      The goal to use
     * @param prevState
     *      The initial state to use.
     * @return A MotionProfile from prevState to goalState that satisfies constraints
     */
    fun generateProfile(constraints: MotionProfileConstraints, goalState: MotionProfileGoal,
                        prevState: MotionState): MotionProfile {
        var deltaPos = goalState.pos() - prevState.pos
        if (deltaPos < 0.0 || (deltaPos == 0.0 && prevState.vel < 0.0)) {
            /*
            For simplicity, we always assume the goal requires positive movement. If negative, we
            flip to solve, then flip the solution.
             */
            return generateFlippedProfile(constraints, goalState, prevState)
        }
        //From here on out we know deltaPos >= 0.0

        //Clamp the start state to be valid
        var startState = MotionState(prevState.t, prevState.pos,  Math.signum(prevState.vel) *
                Math.min(Math.abs(prevState.vel), constraints.maxAbsVel),
                Math.signum(prevState.acc) * Math.min(Math.abs(prevState.acc),
                constraints.maxAbsAcc))

        val profile = MotionProfile()
        profile.reset(startState)

        //If our velocity is headed away from our goal, the first thing we need to do is stop
        if (startState.vel < 0.0 && deltaPos > 0.0) {
            val stoppingTime = Math.abs(startState.vel / constraints.maxAbsAcc)
            profile.appendControl(constraints.maxAbsAcc, stoppingTime)
            startState = profile.endState()
            deltaPos = goalState.pos() - startState.pos
        }
        //From here on out we know startState.vel >= 0.0

        val minAbsVelAtGoalSquared = startState.velSquared - 2.0 * constraints.maxAbsAcc * deltaPos
        val minAbsVelAtGoal = Math.sqrt(Math.abs(minAbsVelAtGoalSquared))
        val maxAbsVelAtGoal = Math.sqrt((startState.velSquared + 2.0 * constraints.maxAbsAcc *
                deltaPos))
        var goalVel = goalState.maxAbsVel()
        var maxAcc = constraints.maxAbsAcc

        if (minAbsVelAtGoalSquared > 0.0 && minAbsVelAtGoal > (goalState.maxAbsVel() +
                        goalState.velTolerance())) {
            /*
             Overshoot is unavoidable with the current constraints. Look at completionBehavior to
             see what we should do.
              */
            when (goalState.completionBehavior()) {
                CompletionBehavior.VIOLATE_MAX_ABS_VEL -> {
                    //Adjust the goal velocity
                    goalVel = minAbsVelAtGoal
                }
                CompletionBehavior.VIOLATE_MAX_ACCEL -> {
                    if (Math.abs(deltaPos) < goalState.posTolerance()) {
                        /*
                        Special case: we are at the goal but moving too fast. This requires
                        'infinite' acceleration which will result in NaNs below, so we can return
                        the profile immediately.
                         */
                        profile.appendSegment(MotionSegment(MotionState(profile.endTime(),
                                profile.endPos(), profile.endState().vel, Double.NEGATIVE_INFINITY),
                                MotionState(profile.endTime(),
                                        profile.endPos(), goalVel,
                                        Double.NEGATIVE_INFINITY)))
                        profile.consolidate()
                        return profile
                    }
                    //Adjust the max acceleration
                    maxAcc = Math.abs(goalVel * goalVel - startState.velSquared) / 2.0 * deltaPos
                }
                CompletionBehavior.OVERSHOOT -> {
                    /*
                    We are going to overshoot the goal, so the first thing we have to do is come to
                    a stop.
                     */
                    val stoppingTime = Math.abs(startState.vel / constraints.maxAbsAcc)
                    profile.appendControl(-constraints.maxAbsAcc, stoppingTime)
                    //Now we need to travel backwards, so we generate a flipped profile
                    profile.appendProfile(generateFlippedProfile(constraints, goalState,
                            profile.endState()))
                    profile.consolidate()
                    return profile
                }
            }
        }
        goalVel = Math.min(goalVel, maxAbsVelAtGoal)

        /*
        From now on we know we can achieve goalVel at goalState.pos exactly using no more than
        +/- maxAcc.
         */

        /*
        What is the maximum velocity we can reach? (Vmax)? This is the interaction of two
        curves: one accelerating towards the goal from profile.endState(), the other coming from
        the goal at max vel (in reverse). If Vmax is greater than constraints.maxAbsVel, we will
        clamp and cruise. Solve the following three equations to find Vmax (by substitution).
        Vmax^2 = Vstart^2 + 2*a*dAccel
        Vgoal^2 = Vmax^2 - 2*a*dDecel
        deltaPos = dAccel + dDecel
         */
        val vMax = Math.min(constraints.maxAbsVel, Math.sqrt((startState.velSquared + goalVel *
                goalVel) / 2.0 + deltaPos * maxAcc))

        //Accelerate to vMax
        if (vMax > startState.vel) {
            val accelTime = (vMax - startState.vel) / maxAcc
            profile.appendControl(maxAcc, accelTime)
            startState = profile.endState()
        }
        //Figure out how much distance will be covered during deceleration
        val distanceDecel = Math.max(0.0, (startState.velSquared - goalVel * goalVel) /
                (2.0 * constraints.maxAbsAcc))
        val distanceCruise = Math.max(0.0, goalState.pos() - startState.pos - distanceDecel)

        //Cruise at constant velocity
        if (distanceCruise > 0.0) {
            val cruiseTime = distanceCruise / startState.vel
            profile.appendControl(0.0, cruiseTime)
        }

        //Decelerate to goal velocity
        if (distanceDecel > 0.0) {
            val decelTime = (startState.vel - goalVel) / maxAcc
            profile.appendControl(-maxAcc, decelTime)
        }

        profile.consolidate()
        return profile
    }
}