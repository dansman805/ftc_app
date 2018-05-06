package com.jdroids.team7026.ftc2017.subsystems

import com.jdroids.team7026.ftc2017.subsystems.loops.Loop
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.Servo

object JewelSystem: Subsystem() {
    enum class JewelKnockerStates {
        KNOCK_LEFT_JEWEL, KNOCK_RIGHT_JEWEL, CENTERED
    }

    enum class JewelArmStates {
        UP, DOWN
    }

    private var jewelKnockerState = JewelKnockerStates.KNOCK_LEFT_JEWEL
    private var jewelArmState = JewelArmStates.UP

    private var jewelKnocker: Servo? = null
    private var jewelArm: Servo? = null

    object ControlLoop: Loop {
        override fun onStart() {
            jewelKnockerState = JewelKnockerStates.KNOCK_LEFT_JEWEL
            jewelArmState = JewelArmStates.UP
        }

        override fun onLoop() {
            jewelKnocker?.position = when (jewelKnockerState) {
                JewelKnockerStates.KNOCK_LEFT_JEWEL -> 0.0
                JewelKnockerStates.KNOCK_RIGHT_JEWEL -> 1.0
                JewelKnockerStates.CENTERED -> 0.5
            }

            jewelArm?.position = when (jewelArmState) {
                JewelArmStates.UP -> 0.9
                JewelArmStates.DOWN -> 0.0
            }
        }

        override fun onStop() {}
    }

    fun setJewelKnockerState(state: JewelKnockerStates) {
        this.jewelKnockerState = state
    }

    fun setJewelArmState(state: JewelArmStates) {
        this.jewelArmState = state
    }

    fun isKnockerAtTarget(): Boolean {
        return when (jewelKnockerState) {
            JewelKnockerStates.KNOCK_LEFT_JEWEL -> jewelKnocker!!.position == 0.0
            JewelKnockerStates.KNOCK_RIGHT_JEWEL -> jewelKnocker!!.position == 1.0
            JewelKnockerStates.CENTERED -> jewelKnocker!!.position == 0.5
        }
    }

    fun isJewelArmAtTarget(): Boolean {
        return when (jewelArmState) {
            JewelArmStates.UP -> jewelArm!!.position == 0.9
            JewelArmStates.DOWN -> jewelArm!!.position == 0.0
        }
    }

    override fun start(opMode: OpMode) {
        this.opMode = opMode

        jewelKnockerState = JewelKnockerStates.KNOCK_LEFT_JEWEL
        jewelArmState = JewelArmStates.UP

        jewelKnocker = this.opMode?.hardwareMap?.get(Servo::class.java, "servoJewelKnock")
        jewelArm = this.opMode?.hardwareMap?.get(Servo::class.java, "servoJewelArm")
    }

    override fun stop() {}

    override fun zeroSensors() {
        //Nothing to do here
    }

    override fun outputToTelemetry() {
        this.opMode?.telemetry?.addData("Jewel Knocker State", jewelKnockerState)
        this.opMode?.telemetry?.addData("Jewel Arm State", jewelArmState)
    }
}