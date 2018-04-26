package com.jdroids.team7026.ftc2017;

import com.jdroids.team7026.lib.util.transformations.DriveSignal;

public class MecanumDriveHelper {
    public DriveSignal angleDrive(double angle, double power, boolean breakMode) {//Angle in radians
        return new DriveSignal(power * Math.cos(angle), -power * Math.sin(angle),
                power * Math.sin(angle), -power * Math.cos(angle), breakMode);
    }
    public DriveSignal angleDrive(double angle, double power) {
        return angleDrive(angle, power, true);
    }

    public DriveSignal arcadeDrive(double forwardsVelocity, double sidewaysVelocity,
                                   double angularVelocity, boolean fieldOrientedDriveEnabled,
                                   double currentRadians, double offset, boolean breakMode) {
        double power = Math.hypot(sidewaysVelocity, forwardsVelocity);

        double angle;
        if(fieldOrientedDriveEnabled) {
            angle = (offset + Math.atan2(forwardsVelocity, sidewaysVelocity) - currentRadians) - Math.PI / 4;
        }
        else {
            angle = Math.atan2(forwardsVelocity, sidewaysVelocity) - Math.PI / 4;
        }

        return new DriveSignal(
                power * Math.cos(angle) + angularVelocity,
                -power * Math.sin(angle) - angularVelocity,
                power * Math.sin(angle) + angularVelocity,
                -power * Math.cos(angle) - angularVelocity);
    }

    public DriveSignal arcadeDrive(double forwardsVelocity, double sidewaysVelocity,
                                   double angularVelocity, boolean fieldOrientedDriveEnabled,
                                   double currentRadians, double offset) {
        return arcadeDrive(forwardsVelocity, sidewaysVelocity, angularVelocity,
                fieldOrientedDriveEnabled, currentRadians, offset);
    }

    public DriveSignal arcadeDrive(double forwardsVelocity, double sidewaysVelocity,
                                   double angularVelocity, boolean fieldOrientedDriveEnabled,
                                   double currentRadians) {
        return arcadeDrive(forwardsVelocity, sidewaysVelocity, angularVelocity,
                fieldOrientedDriveEnabled, currentRadians, 0, true);
    }

    public DriveSignal arcadeDrive(double forwardsVelocity, double sidewaysVelocity,
                                   double angularVelocity) {
        return arcadeDrive(forwardsVelocity, sidewaysVelocity, angularVelocity,
                false, 0, 0);
    }
}
