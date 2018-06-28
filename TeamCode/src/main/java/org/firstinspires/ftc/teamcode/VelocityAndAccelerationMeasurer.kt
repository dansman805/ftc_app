package org.firstinspires.ftc.teamcode

import com.jdroids.team7026.ftc2017.Robot
import com.jdroids.team7026.lib.util.DriveSignal
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name="Velocity Measurer")
class VelocityAndAccelerationMeasurer: OpMode() {
    override fun init() {
        Robot.start(this)
    }

    override fun start() {
        Robot.ControlLoop.onStart()
    }

    override fun loop() {
        if (gamepad1.a) {
            Robot.drive.setOpenLoop(DriveSignal(-1.0, 1.0))
            Robot.outputToTelemetry()
        }
        else {
            Robot.drive.setOpenLoop(DriveSignal.BRAKE)
        }
    }
}