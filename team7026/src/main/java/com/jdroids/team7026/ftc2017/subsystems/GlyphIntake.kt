package com.jdroids.team7026.ftc2017.subsystems

import com.jdroids.team7026.ftc2017.subsystems.loops.Loop
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DigitalChannel
import com.qualcomm.robotcore.hardware.Servo
import javax.crypto.spec.OAEPParameterSpec

object GlyphIntake: Subsystem() {
    enum class LiftState {
        NEUTRAL,
        UP,
        DOWN,
    }

    enum class GrabberState {
        CLOSED,
        OPEN,
        WIDE_OPEN,
        INIT
    }

    private var firstGlyphLift: DcMotorEx? = null
    private var secondGlyphLift: DcMotorEx? = null

    private var firstLiftTopSwitch: DigitalChannel? = null
    private var firstLiftBottomSwitch: DigitalChannel? = null
    private var secondLiftTopSwitch: DigitalChannel? = null
    private var secondLiftBottomSwitch: DigitalChannel? = null

    private var grabberTL: Servo? = null
    private var grabberTR: Servo? = null
    private var grabberBL: Servo? = null
    private var grabberBR: Servo? = null

    private var firstLiftState: LiftState? = null
    private var secondLiftState: LiftState? = null

    private var topGrabberState: GrabberState? = null
    private var bottomGrabberState: GrabberState? = null

    private object ControlLoop: Loop {
        override fun onStart() {
            setBothGlyphLiftStates(LiftState.NEUTRAL)
        }

        override fun onLoop() {
            when (firstLiftState) {
                LiftState.NEUTRAL -> firstGlyphLift?.setPower(0.0)
                LiftState.UP -> if(firstLiftBottomSwitch?.getState() == true) {
                    firstGlyphLift?.setPower(1.0)
                }
                else {
                    setFirstGlyphLiftState(LiftState.NEUTRAL)
                }
                LiftState.DOWN -> if(firstLiftTopSwitch?.getState() == true) {
                    firstGlyphLift?.setPower(-1.0)
                }
                else {
                    setFirstGlyphLiftState(LiftState.NEUTRAL)
                }
            }

            when (secondLiftState) {
                LiftState.NEUTRAL -> secondGlyphLift?.setPower(0.0)
                LiftState.UP -> if(secondLiftBottomSwitch?.getState() == true) {
                    secondGlyphLift?.setPower(1.0)
                }
                else {
                    setSecondGlyphLiftState(LiftState.NEUTRAL)
                }
                LiftState.DOWN -> if(secondLiftTopSwitch?.getState() == true) {
                    secondGlyphLift?.setPower(-1.0)
                }
                else {
                    setSecondGlyphLiftState(LiftState.NEUTRAL)
                }
            }

            when (topGrabberState) {
                GlyphIntake.GrabberState.CLOSED -> {
                    grabberTL?.position = 0.9
                    grabberTR?.position = 0.3
                }
                GlyphIntake.GrabberState.OPEN -> {
                    grabberTL?.position = 0.35
                    grabberTR?.position = 0.7
                }
                GlyphIntake.GrabberState.WIDE_OPEN -> {
                    grabberTL?.position = 0.5
                    grabberTR?.position = 0.65
                }
                GlyphIntake.GrabberState.INIT -> {
                    grabberTL?.position = 0.9
                    grabberTR?.position = 0.3
                }
            }

            when (bottomGrabberState) {
                GlyphIntake.GrabberState.CLOSED -> {
                    grabberBL?.position = 0.25
                    grabberBR?.position = 0.4
                }
                GlyphIntake.GrabberState.OPEN -> {
                    grabberBL?.position = 0.45
                    grabberBR?.position = 0.25
                }
                GlyphIntake.GrabberState.WIDE_OPEN -> {
                    grabberBL?.position = 0.55
                    grabberBR?.position = 0.2
                }
                GlyphIntake.GrabberState.INIT -> {
                    grabberBL?.position = 1.0
                    grabberBR?.position = 0.0
                }
            }
        }

        override fun onStop() {
            setBothGlyphLiftStates(LiftState.NEUTRAL)
        }
    }

    override fun start(opMode: OpMode) {
        this.opMode = opMode
        setBothGlyphLiftStates(LiftState.NEUTRAL)
    }

    override fun stop() {
        setBothGlyphLiftStates(LiftState.NEUTRAL)
    }

    override fun outputToTelemetry() {
        this.opMode?.telemetry?.addData("Lift States",
                "FirstLift: " + firstLiftState +
                        " SecondLift: " + secondLiftState)
        this.opMode?.telemetry?.addData("Grabber States",
                "TopGrabber: " + topGrabberState +
                " BottomGrabber: " + bottomGrabberState)
    }

    override fun zeroSensors() {
        //Nothing to zero on this subsystem
    }

    fun setFirstGlyphLiftState(liftState: LiftState) {
        firstLiftState = liftState
    }

    fun setSecondGlyphLiftState(liftState: LiftState) {
        secondLiftState = liftState
    }

    fun setBothGlyphLiftStates(liftState: LiftState) {
        setFirstGlyphLiftState(liftState)
        setSecondGlyphLiftState(liftState)
    }

    fun setTopGrabberState(grabberState: GrabberState) {
        topGrabberState = grabberState
    }


    fun setBottomGrabberState(grabberState: GrabberState) {
        bottomGrabberState = grabberState
    }

    fun setBothGrabberStates(grabberState: GrabberState) {
        setTopGrabberState(grabberState)
        setBottomGrabberState(grabberState)
    }
}