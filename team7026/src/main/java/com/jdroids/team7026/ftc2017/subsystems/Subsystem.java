package com.jdroids.team7026.ftc2017.subsystems;

/**
 * The Subsystem abstract class, which serves as a basic framework for all robot
 * subsystems. Each subsystem outputs commands to telemetry, has a stop
 * routine (for after each match), and a routine to zero all sensors, which
 * helps with calibration.
 *
 * Each subsystem is initialized once as a robot does not contain more than one of the same
 * subsystem; a robot does not have 2 drivetrains. Each subsystem should instantiate its member
 * components. They are state machines with a current state and desired state; the code will try to
 * match the two states with corresponding actions
 */
public abstract class Subsystem {
    public abstract void outputToTelemetry();
    public abstract void stop();
}
