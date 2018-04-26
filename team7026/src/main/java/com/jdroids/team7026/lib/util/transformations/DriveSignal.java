package com.jdroids.team7026.lib.util.transformations;

/**
 * A drivetrain command consisting of the front left, front right, back left, and back right motor
 * settings and whether the brake mode is enabled.
 */
public class DriveSignal {
    public double frontLeftMotor;
    public double frontRightMotor;
    public double backLeftMotor;
    public double backRightMotor;
    public boolean breakMode;

    public DriveSignal(double left, double right) {
        this(left, right, true);
    }

    public DriveSignal(double left, double right, boolean breakMode) {
        this(left, right, left, right, breakMode);
    }

    public DriveSignal(double frontLeft, double frontRight, double backLeft, double backRight) {
        this(frontLeft, frontRight, backLeft, backRight, true);
    }

    public DriveSignal(double frontLeft, double frontRight, double backLeft, double backRight,
                       boolean breakMode) {
        this.frontLeftMotor = frontLeft;
        this.frontRightMotor = frontRight;
        this.backLeftMotor = backLeft;
        this.backRightMotor = backRight;
        this.breakMode = breakMode;
    }

    public static DriveSignal NEUTRAL = new DriveSignal(0, 0);
    public static DriveSignal BREAK = new DriveSignal(0, 0, true);

    @Override
    public String toString() {
        return "FL: " + frontLeftMotor + ", FR: " + frontRightMotor
                + ", BL: " + backLeftMotor + ", BR: " + backRightMotor;
    }
}
