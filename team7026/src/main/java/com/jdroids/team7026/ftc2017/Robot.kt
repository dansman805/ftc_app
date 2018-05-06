package com.jdroids.team7026.ftc2017

import com.jdroids.team7026.ftc2017.auto.commands.DetectJewel
import com.jdroids.team7026.ftc2017.subsystems.Drive
import com.jdroids.team7026.ftc2017.subsystems.GlyphIntake
import com.jdroids.team7026.ftc2017.subsystems.JewelSystem
import com.jdroids.team7026.ftc2017.subsystems.Subsystem
import com.jdroids.team7026.ftc2017.subsystems.loops.Loop
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark

object Robot : Subsystem(){
    //Subsystems
    val drive = Drive
    val glyphIntake = GlyphIntake
    val jewelSystem = JewelSystem

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

        drive.start(this.opMode as OpMode)
        glyphIntake.start(this.opMode as OpMode)
        jewelSystem.start(this.opMode as OpMode)
    }

    override fun outputToTelemetry() {
        drive.outputToTelemetry()
        glyphIntake.outputToTelemetry()
        jewelSystem.outputToTelemetry()
        this.opMode?.telemetry?.update()
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