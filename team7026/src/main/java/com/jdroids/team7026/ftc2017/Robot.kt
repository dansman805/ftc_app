package com.jdroids.team7026.ftc2017

import com.acmerobotics.dashboard.RobotDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.jdroids.team7026.ftc2017.auto.commands.DetectJewel
import com.jdroids.team7026.ftc2017.subsystems.Drive
import com.jdroids.team7026.ftc2017.subsystems.GlyphIntake
import com.jdroids.team7026.ftc2017.subsystems.JewelSystem
import com.jdroids.team7026.ftc2017.subsystems.Subsystem
import com.jdroids.team7026.ftc2017.subsystems.loops.Loop
import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry



object Robot : Subsystem(){
    //Dashboard stuff
    private val dashboard: RobotDashboard = RobotDashboard.getInstance()
    var telemetryPacket = TelemetryPacket()

    //Subsystems
    val drive = Drive
    val glyphIntake = GlyphIntake
    val jewelSystem = JewelSystem

    //Miscellaneous Sensors
    var imu: BNO055IMU? = null

    class GlobalValues {
        var vumark = RelicRecoveryVuMark.UNKNOWN
        var jewelOnLeft = DetectJewel.Color.NONE
    }

    val globalValues = GlobalValues()

    //Helper classes
    val mecanumDriveHelper = MecanumDriveHelper

    object ControlLoop: Loop {
        override fun onStart() {
            Drive.ControlLoop.onStart()
            GlyphIntake.ControlLoop.onStart()
            JewelSystem.ControlLoop.onStart()
        }

        override fun onLoop() {
            Drive.ControlLoop.onLoop()
            GlyphIntake.ControlLoop.onLoop()
            JewelSystem.ControlLoop.onLoop()
        }

        override fun onStop() {
            Drive.ControlLoop.onStop()
            GlyphIntake.ControlLoop.onStop()
            JewelSystem.ControlLoop.onStop()
        }
    }

    override fun start(opMode: OpMode) {
        this.opMode = opMode

        imu = this.opMode?.hardwareMap?.get(BNO055IMU::class.java, "imu")

        val parameters: BNO055IMU.Parameters = BNO055IMU.Parameters()

        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"
        parameters.loggingEnabled = true
        parameters.loggingTag = "IMU"

        imu!!.initialize(parameters)

        drive.start(this.opMode as OpMode)
        glyphIntake.start(this.opMode as OpMode)
        jewelSystem.start(this.opMode as OpMode)
    }

    override fun outputToTelemetry() {
        telemetryPacket = TelemetryPacket()

        drive.outputToTelemetry()
        glyphIntake.outputToTelemetry()
        jewelSystem.outputToTelemetry()
        this.opMode?.telemetry?.update()

        dashboard.sendTelemetryPacket(telemetryPacket)
    }

    override fun zeroSensors() {
        drive.zeroSensors()
        glyphIntake.zeroSensors()
        jewelSystem.zeroSensors()
    }

    override fun stop() {
        drive.stop()
        glyphIntake.stop()
        jewelSystem.stop()
    }
}