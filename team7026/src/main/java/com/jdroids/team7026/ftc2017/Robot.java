package com.jdroids.team7026.ftc2017;

import com.jdroids.team7026.ftc2017.subsystems.Drive;
import com.jdroids.team7026.ftc2017.subsystems.Subsystem;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * The main robot class that instantiates all subsystems and helper classes.
 */

public class Robot extends Subsystem{
    //Subsystems
    Drive drive;

    //Helper classes
    MecanumDriveHelper mecanumDriveHelper;

    private OpMode opMode;

    public Robot(OpMode opMode) {
        this.opMode = opMode;
    }

    public void stop() {
        drive.stop();
    }

    public void outputToTelemetry() {
        drive.outputToTelemetry();

        this.opMode.telemetry.update();
    }

    public void zeroSensors() {
        drive.zeroSensors();
    }

    /**
     * This function is run when the robot is started up and should be used for initialization code.
     */
    public void robotInit() {
        try {
            drive = new Drive(this.opMode);

            zeroSensors();
        }
        catch (Throwable t) {
            throw t;
        }
    }
}
