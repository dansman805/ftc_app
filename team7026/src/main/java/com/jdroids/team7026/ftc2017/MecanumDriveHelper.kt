package com.jdroids.team7026.ftc2017

import com.jdroids.team7026.lib.util.DriveSignal
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference

object MecanumDriveHelper {
    fun angleDrive(angle: Double, power: Double, brakeMode: Boolean = false): DriveSignal {
        //Angle is in radians
        return DriveSignal(power * Math.cos(angle), -power * Math.sin(angle),
                power * Math.sin(angle), -power * Math.cos(angle), brakeMode)
    }

    fun arcadeDrive(forwardsVelocity: Double, sidewaysVelocity: Double,
                    angularVelocity: Double, fieldOrientedDriveEnabled: Boolean = false,
                    offset: Double = 0.0, brakeMode: Boolean = false): DriveSignal {
        val power = Math.hypot(sidewaysVelocity, forwardsVelocity)


        val currentRadians = if(fieldOrientedDriveEnabled) {
            Robot.imu!!.angularOrientation.toAxesReference(AxesReference.INTRINSIC)
                    .toAxesOrder(AxesOrder.ZYX).toAngleUnit(AngleUnit.RADIANS).firstAngle.toDouble()
        }
        else {0.0}

        val angle = offset + Math.atan2(forwardsVelocity, sidewaysVelocity) - currentRadians - Math.PI / 4



        return DriveSignal(
                power * Math.cos(angle) - angularVelocity,
                -power * Math.sin(angle) - angularVelocity,
                power * Math.sin(angle) - angularVelocity,
                -power * Math.cos(angle) - angularVelocity)
    }
}