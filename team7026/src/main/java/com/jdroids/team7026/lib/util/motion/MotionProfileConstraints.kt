package com.jdroids.team7026.lib.util.motion

/**
 * Constraints for constructing a MotionProfile
 */
class MotionProfileConstraints(maxAbsVel: Double, maxAbsAcc: Double) {
    val maxAbsVel: Double
    val maxAbsAcc: Double

    init {
        this.maxAbsVel = Math.abs(maxAbsVel)
        this.maxAbsAcc = Math.abs(maxAbsAcc)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is MotionProfileConstraints) {
            return false
        }
        return this.maxAbsVel == other.maxAbsVel && this.maxAbsAcc == other.maxAbsAcc
    }
}