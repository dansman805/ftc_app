package com.jdroids.team7026.ftc2017

import com.jdroids.team7026.ftc2017.auto.commands.JewelDetector
import com.jdroids.team7026.ftc2017.subsystems.Drive
import com.jdroids.team7026.ftc2017.subsystems.GlyphIntake
import com.jdroids.team7026.ftc2017.subsystems.Subsystem
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark

object Robot : Subsystem(){
    //Subsystems
    val drive = Drive
    val glyphIntake = GlyphIntake

    class GlobalValues {
        var vumark = RelicRecoveryVuMark.UNKNOWN
        var jewelOnLeft = JewelDetector.Color.NONE
    }

    val globalValues = GlobalValues()

    //Helper classes
    val mecanumDriveHelper = MecanumDriveHelper

    override fun start(opMode: OpMode) {
        this.opMode = opMode

        drive.start(this.opMode as OpMode)
        glyphIntake.start(this.opMode as OpMode)
    }

    override fun outputToTelemetry() {
        drive.outputToTelemetry()
        glyphIntake.outputToTelemetry()
        this.opMode?.telemetry?.update()
    }

    override fun zeroSensors() {
        drive.zeroSensors()
        glyphIntake.zeroSensors()
    }

    override fun stop() {
        drive.stop()
        glyphIntake.stop()
    }
}