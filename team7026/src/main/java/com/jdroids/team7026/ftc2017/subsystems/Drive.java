package com.jdroids.team7026.ftc2017.subsystems;

import android.util.Log;

import com.jdroids.team7026.ftc2017.subsystems.loops.Loop;
import com.jdroids.team7026.lib.util.DriveSignal;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

/**
 * The drivetrain of the robot, which implements the Subsystem abstract class. The drivetrain has
 * several states and provides various control methods
 *
 * @see com.jdroids.team7026.ftc2017.subsystems.Subsystem
 */

public class Drive extends Subsystem {
    public enum DriveControlState {
        OPEN_LOOP, BASE_LOCKED
    }


    private boolean isBrakeMode = true;
    private int[] baseLockPositions = new int[4];

    private DcMotorEx frontLeftMotor;
    private DcMotorEx frontRightMotor;
    private DcMotorEx backLeftMotor;
    private DcMotorEx backRightMotor;

    private DriveControlState driveControlState;

    //The main control loop for the drivetrain which cycles through different states
    private final Loop loop = new Loop() {
        @Override
        public void onStart() {
            setOpenLoop(DriveSignal.NEUTRAL);
        }

        @Override
        public void onLoop() {
            switch (driveControlState) {
                case OPEN_LOOP:
                    return;
                case BASE_LOCKED:
                    frontLeftMotor.setTargetPosition(baseLockPositions[0]);
                    frontRightMotor.setTargetPosition(baseLockPositions[1]);
                    backLeftMotor.setTargetPosition(baseLockPositions[2]);
                    backRightMotor.setTargetPosition(baseLockPositions[3]);

                    break;
                default:
                    Log.e("Drive", "Unexpected drive control state: " + driveControlState);
                    break;
            }
        }

        @Override
        public void onStop() {
            setOpenLoop(DriveSignal.NEUTRAL);
        }
    };

    public synchronized void stop() {
        setOpenLoop(DriveSignal.NEUTRAL);
    }

    @Override
    public void outputToTelemetry() {
        this.opMode.telemetry.addData("Motor Powers",
                "FL: " + frontLeftMotor.getPower() +
                        " FR: " + frontRightMotor.getPower() +
                        " BL: " + backLeftMotor.getPower() +
                        " BR: " + backRightMotor.getPower());
    }

    public void zeroSensors() {
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    public Drive(OpMode opMode) {
        this.opMode = opMode;

        frontLeftMotor = this.opMode.hardwareMap.get(DcMotorEx.class, "FrontLeft");
        frontRightMotor = this.opMode.hardwareMap.get(DcMotorEx.class, "FrontRight");
        backLeftMotor = this.opMode.hardwareMap.get(DcMotorEx.class, "BackLeft");
        backRightMotor = this.opMode.hardwareMap.get(DcMotorEx.class, "BackRight");
        setRunMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        driveControlState = DriveControlState.OPEN_LOOP;
    }

    public Loop getLoop() {
        return loop;
    }

    protected synchronized void setPower(double frontLeft, double frontRight, double backLeft,
                            double backRight) {
        frontLeftMotor.setPower(frontLeft);
        frontRightMotor.setPower(frontRight);
        backLeftMotor.setPower(backLeft);
        backRightMotor.setPower(backRight);
    }

    public synchronized void setOpenLoop(DriveSignal signal) {
        if(driveControlState != DriveControlState.OPEN_LOOP) {
            setRunMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
            driveControlState = DriveControlState.OPEN_LOOP;
        }
        setBrakeMode(signal.brakeMode);
        setPower(signal.frontLeft, signal.frontRight, signal.backLeft, signal.backRight);
    }

    synchronized void setRunMode(DcMotorEx.RunMode runMode) {
        frontLeftMotor.setMode(runMode);
        frontRightMotor.setMode(runMode);
        backLeftMotor.setMode(runMode);
        backRightMotor.setMode(runMode);
    }

    public synchronized void setBrakeMode(boolean on) {
        if(on && !isBrakeMode) {
            frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            isBrakeMode = on;
        }
        else if(!on && isBrakeMode) {
            frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
    }

    public synchronized void setBaseLockOn() {
        if(driveControlState != DriveControlState.BASE_LOCKED) {
            setRunMode(DcMotorEx.RunMode.RUN_TO_POSITION);


            baseLockPositions[0] = frontLeftMotor.getCurrentPosition();
            baseLockPositions[1] = frontRightMotor.getCurrentPosition();
            baseLockPositions[2] = backLeftMotor.getCurrentPosition();
            baseLockPositions[3] = backRightMotor.getCurrentPosition();

            driveControlState = DriveControlState.BASE_LOCKED;
        }

    }
}
