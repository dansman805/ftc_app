package com.jdroids.team7026.lib.util;

/**
 * A drivetrain command consisting of the front left, front right, back left, and back right motor
 * settings and whether the brake mode is enabled.
 */
public class DriveSignal {
    public double frontLeft;
    public double frontRight;
    public double backLeft;
    public double backRight;
    public boolean brakeMode;

    public DriveSignal(double left, double right) {
        this(left, right, false);
    }

    public DriveSignal(double left, double right, boolean brakeMode) {
        this(left, right, left, right, brakeMode);
    }

    public DriveSignal(double frontLeft, double frontRight, double backLeft, double backRight) {
        this(frontLeft, frontRight, backLeft, backRight, false);
    }

    public DriveSignal(double frontLeft, double frontRight, double backLeft, double backRight,
                       boolean brakeMode) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
        this.brakeMode = brakeMode;
    }

    public static DriveSignal NEUTRAL = new DriveSignal(0, 0, false);
    public static DriveSignal BREAK = new DriveSignal(0, 0, true);

    @Override
    public String toString() {
        return "FL: " + frontLeft + ", FR: " + frontRight
                + ", BL: " + backLeft + ", BR: " + backRight;
    }
}
