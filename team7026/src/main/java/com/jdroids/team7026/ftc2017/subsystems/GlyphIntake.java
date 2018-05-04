package com.jdroids.team7026.ftc2017.subsystems;

import android.util.Log;

import com.jdroids.team7026.ftc2017.subsystems.loops.Loop;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

public class GlyphIntake extends Subsystem{
    public enum LiftState {
        NEUTRAL,
        UP,
        DOWN,
    }

    public enum GrabberState {
        CLOSED,
        OPEN,
        WIDE_OPEN,
        INIT
    }

    private DcMotorEx firstGlyphLift;
    private DcMotorEx secondGlyphLift;

    private DigitalChannel firstLiftTopSwitch;
    private DigitalChannel firstLiftBottomSwitch;
    private DigitalChannel secondLiftTopSwitch;
    private DigitalChannel secondLiftBottomSwitch;

    private Servo grabberTL;
    private Servo grabberTR;
    private Servo grabberBL;
    private Servo grabberBR;

    private LiftState firstLiftState;
    private LiftState secondLiftState;

    private GrabberState topGrabberState;
    private GrabberState bottomGrabberState;

    private final Loop loop = new Loop() {
        @Override
        public void onStart() {
            setBothGlyphLiftStates(LiftState.NEUTRAL);
        }

        @Override
        public void onLoop() {
            switch (firstLiftState) {
                case NEUTRAL:
                    firstGlyphLift.setPower(0);
                    break;
                case UP:
                    if(firstLiftBottomSwitch.getState()) {
                        firstGlyphLift.setPower(1);
                    }
                    else {
                        setFirstGlyphLiftState(LiftState.NEUTRAL);
                    }
                    break;
                case DOWN:
                    if(firstLiftTopSwitch.getState()) {
                        firstGlyphLift.setPower(-1);
                    }
                    else {
                        setFirstGlyphLiftState(LiftState.NEUTRAL);
                    }
                    break;
            }

            switch (secondLiftState) {
                case NEUTRAL:
                    secondGlyphLift.setPower(0);
                    break;
                case UP:
                    if(secondLiftBottomSwitch.getState()) {
                        secondGlyphLift.setPower(1);
                    }
                    else {
                        setSecondGlyphLiftState(LiftState.NEUTRAL);
                    }
                    break;
                case DOWN:
                    if(secondLiftTopSwitch.getState()) {
                        secondGlyphLift.setPower(-1);
                    }
                    else {
                        setSecondGlyphLiftState(LiftState.NEUTRAL);
                    }
                    break;
            }

            switch (topGrabberState) {
                case CLOSED:
                    grabberTL.setPosition(0.9);
                    grabberTR.setPosition(0.3);
                    break;
                case OPEN:
                    grabberTL.setPosition(0.35);
                    grabberTR.setPosition(0.7);
                    break;
                case WIDE_OPEN:
                    grabberTL.setPosition(0.5);
                    grabberTR.setPosition(0.65);
                    break;
                case INIT:
                    grabberTL.setPosition(0.9);
                    grabberTR.setPosition(0.3);
                    break;
            }

            switch (bottomGrabberState) {
                case CLOSED:
                    grabberBL.setPosition(0.25);
                    grabberBR.setPosition(0.4);
                    break;
                case OPEN:
                    grabberBL.setPosition(0.45);
                    grabberBR.setPosition(0.25);
                    break;
                case WIDE_OPEN:
                    grabberBL.setPosition(0.55);
                    grabberBR.setPosition(0.2);
                    break;
                case INIT:
                    grabberBL.setPosition(1.0);
                    grabberBR.setPosition(0.0);
                    break;
            }
        }

        @Override
        public void onStop() {
            setBothGlyphLiftStates(LiftState.NEUTRAL);
        }
    };

    @Override
    public synchronized void stop() {
        setBothGlyphLiftStates(LiftState.NEUTRAL);
    }

    @Override
    public void outputToTelemetry() {
        this.opMode.telemetry.addData("Lift States",
                "FirstLift: " + firstLiftState.name() +
                        " SecondLift: " + secondLiftState.name());
        this.opMode.telemetry.addData("Grabber States",
                "TopGrabber: " + topGrabberState +
                        " BottomGrabber: " + bottomGrabberState);
    }

    @Override
    public void zeroSensors() {
        //Nothing goes here
    }

    public void setFirstGlyphLiftState(LiftState liftState) {
        firstLiftState = liftState;
    }

    public void setSecondGlyphLiftState(LiftState liftState) {
        secondLiftState = liftState;
    }

    public void setBothGlyphLiftStates(LiftState liftState) {
        setFirstGlyphLiftState(liftState);
        setSecondGlyphLiftState(liftState);
    }

    public void setTopGrabberState(GrabberState grabberState) {
        topGrabberState = grabberState;
    }


    public void setBottomGrabberState(GrabberState grabberState) {
        bottomGrabberState = grabberState;
    }

    public void setBothGrabberStates(GrabberState grabberState) {
        setTopGrabberState(grabberState);
        setBottomGrabberState(grabberState);
    }

}
