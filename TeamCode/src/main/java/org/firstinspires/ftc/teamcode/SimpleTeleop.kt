package org.firstinspires.ftc.teamcode

import com.jdroids.team7026.ftc2017.Robot
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name="SimpleTeleop")
class SimpleTeleop: OpMode() {
    override fun init() {
        Robot.start(this)
    }

    override fun start() {
        Robot.ControlLoop.onStart()
    }

    override fun loop() {
        Robot.drive.setOpenLoop(Robot.mecanumDriveHelper.arcadeDrive(
                gamepad1.left_stick_y.toDouble(),  -gamepad1.left_stick_x.toDouble(),
                gamepad1.right_stick_x.toDouble(), true))
        Robot.ControlLoop.onLoop()
        Robot.outputToTelemetry()
    }

    override fun stop() {
        Robot.ControlLoop.onStop()
    }
}