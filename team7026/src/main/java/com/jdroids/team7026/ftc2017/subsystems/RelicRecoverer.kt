package com.jdroids.team7026.ftc2017.subsystems

import com.jdroids.team7026.ftc2017.subsystems.loops.Loop
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo

class RelicRecoverer(): Subsystem() {
    private var relicExtender: DcMotorEx? = null
    private var relicRotationalActuator: Servo? = null
    private var relicExtensionActuator: Servo? = null


    object ControlLoop: Loop {
        override fun onStart() {

        }

        override fun onLoop() {

        }

        override fun onStop() {

        }
    }

    override fun start(opMode: OpMode) {
        this.opMode = opMode
    }

    override fun stop() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun zeroSensors() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun outputToTelemetry() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}