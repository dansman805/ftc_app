package com.jdroids.team7026.lib.util

class DriveSignal(var frontLeft: Double, var frontRight: Double, var backLeft: Double,
                  var backRight: Double, var brakeMode: Boolean = false) {
    constructor(left: Double, right: Double, brakeMode: Boolean = false) :
            this(left, right, left, right, brakeMode)

    override fun toString(): String{
        return "FL: $frontLeft, FR: $frontRight, BL: $backLeft, BR: $backRight"
    }

    companion object {
        val NEUTRAL = DriveSignal(0.0, 0.0)
        val BRAKE = DriveSignal(0.0, 0.0, true)
    }
}