package com.jdroids.team7026.ftc2017

import com.jdroids.team7026.lib.util.DriveSignal

object MecanumDriveHelper {
    fun angleDrive(angle: Double, power: Double, brakeMode: Boolean = false): DriveSignal {
        //Angle is in radians
        return DriveSignal(power * Math.cos(angle), -power * Math.sin(angle),
                power * Math.sin(angle), -power * Math.cos(angle), brakeMode)
    }

    fun arcadeDrive(forwardsVelocity: Double, sidewaysVelocity: Double,
                    angularVelocity: Double, fieldOrientedDriveEnabled: Boolean = false,
                    currentRadians: Double = 0.0, offset: Double = 0.0,
                    breakMode: Boolean = false): DriveSignal {
        val power = Math.hypot(sidewaysVelocity, forwardsVelocity)

        val angle: Double = when (fieldOrientedDriveEnabled) {
            true -> offset + Math.atan2(forwardsVelocity, sidewaysVelocity) - currentRadians - Math.PI / 4
            false -> Math.atan2(forwardsVelocity, sidewaysVelocity) - Math.PI / 4
        }

        return DriveSignal(
                power * Math.cos(angle) + angularVelocity,
                -power * Math.sin(angle) - angularVelocity,
                power * Math.sin(angle) + angularVelocity,
                -power * Math.cos(angle) - angularVelocity)
    }
}