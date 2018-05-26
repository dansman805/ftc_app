package com.jdroids.team7026.ftc2017.subsystems

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.jdroids.team7026.ftc2017.subsystems.loops.Loop
import com.jdroids.team7026.lib.util.DriveSignal
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.jdroids.team7026.ftc2017.Robot
import jaci.pathfinder.Pathfinder
import jaci.pathfinder.Trajectory
import jaci.pathfinder.Waypoint
import jaci.pathfinder.followers.EncoderFollower
import jaci.pathfinder.modifiers.TankModifier
import com.qualcomm.robotcore.hardware.PIDCoefficients



object Drive: Subsystem() {
    enum class DriveControlState {
        OPEN_LOOP, BASE_LOCKED, FOLLOW_PATH
    }

    private var isBrakeMode = false
    private val baseLockPositions = arrayOf(0, 0, 0, 0)

    private var frontLeftMotor: DcMotorEx? = null
    private var frontRightMotor: DcMotorEx? = null
    private var backLeftMotor: DcMotorEx? = null
    private var backRightMotor: DcMotorEx? = null

    private val trajectoryConfig = Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC,
            Trajectory.Config.SAMPLES_HIGH, 0.05, 1.0, 1.0,
            1.0)
            /*max_velocity, max_acceleration, and max_jerk have to be replaced with real values.
            Reference https://www.chiefdelphi.com/forums/showthread.php?p=1741216
            for how to find these.
             */
    private var leftEncoderFollower: EncoderFollower? = null
    private var rightEncoderFollower: EncoderFollower? = null

    private var driveControlState : DriveControlState = DriveControlState.OPEN_LOOP

    object ControlLoop : Loop {
        override fun onStart() {
            setOpenLoop(DriveSignal.NEUTRAL)
        }

        override fun onLoop() {
            when (driveControlState) {
                DriveControlState.OPEN_LOOP -> return

                DriveControlState.BASE_LOCKED -> {
                    frontLeftMotor?.targetPosition = baseLockPositions[0]
                    frontRightMotor?.targetPosition = baseLockPositions[1]
                    backLeftMotor?.targetPosition = baseLockPositions[2]
                    backRightMotor?.targetPosition = baseLockPositions[3]
                }

                DriveControlState.FOLLOW_PATH -> {
                    frontLeftMotor!!.power = leftEncoderFollower!!.calculate(
                            getLeftSideEncoderPosition())
                    backLeftMotor!!.power = leftEncoderFollower!!.calculate(
                            getLeftSideEncoderPosition())
                    frontRightMotor!!.power = rightEncoderFollower!!.calculate(
                            getRightSideEncoderPosition())
                    backRightMotor!!.power = rightEncoderFollower!!.calculate(
                            getRightSideEncoderPosition())
                }
            }
        }

        override fun onStop() {
            setOpenLoop(DriveSignal.NEUTRAL)
        }
    }

    override fun start(opMode: OpMode) {
        this.opMode = opMode

        frontLeftMotor = opMode.hardwareMap.get(DcMotorEx::class.java, "FrontLeft")
        frontRightMotor = opMode.hardwareMap.get(DcMotorEx::class.java, "FrontRight")
        backLeftMotor = opMode.hardwareMap.get(DcMotorEx::class.java, "BackLeft")
        backRightMotor = opMode.hardwareMap.get(DcMotorEx::class.java, "BackRight")

        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER)

        driveControlState = DriveControlState.OPEN_LOOP
    }

    override fun stop() {
        setOpenLoop(DriveSignal.NEUTRAL)
    }

    override fun outputToTelemetry() {
        this.opMode?.telemetry?.addData("Drive Mode", driveControlState)
        this.opMode?.telemetry?.addData("Motor Powers",
                "FL: " + frontLeftMotor?.power +
                " FR: " + frontRightMotor?.power +
                " BL: " + backLeftMotor?.power +
                " BR: " + backRightMotor?.power)

        Robot.telemetryPacket.put("Drive Mode", driveControlState)

        Robot.telemetryPacket.put("Front Left Motor Power", frontLeftMotor?.power)
        Robot.telemetryPacket.put("Front Right Motor Power", frontRightMotor?.power)
        Robot.telemetryPacket.put("Back Left Motor Power", backLeftMotor?.power)
        Robot.telemetryPacket.put("Back Right Motor Power", backRightMotor?.power)

        Robot.telemetryPacket.put("Front Left Motor Position", frontLeftMotor?.currentPosition)
        Robot.telemetryPacket.put("Front Right Motor Position   ", frontRightMotor?.currentPosition)
        Robot.telemetryPacket.put("Back Left Motor Position", backLeftMotor?.currentPosition)
        Robot.telemetryPacket.put("Back Right Motor Position", backRightMotor?.currentPosition)

    }

    override fun zeroSensors() {
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER)
        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER)
    }

    private fun setPower(frontLeft: Double, frontRight: Double, backLeft: Double, backRight: Double) {
        frontLeftMotor?.power = frontLeft
        frontRightMotor?.power = frontRight
        backLeftMotor?.power = backLeft
        backRightMotor?.power = backRight
    }

    fun setOpenLoop(signal: DriveSignal) {
        if (driveControlState != DriveControlState.OPEN_LOOP) {
            setRunMode(DcMotor.RunMode.RUN_USING_ENCODER)
            driveControlState = DriveControlState.OPEN_LOOP
        }

        setBrakeMode(signal.brakeMode)
        setPower(signal.frontLeft, signal.frontRight, signal.backLeft, signal.backRight)
    }

    private fun setRunMode(runMode: DcMotor.RunMode) {
        frontLeftMotor?.mode = runMode
        frontRightMotor?.mode = runMode
        backLeftMotor?.mode = runMode
        backRightMotor?.mode = runMode
    }

    private fun setBrakeMode (on: Boolean) {
        if (on && !isBrakeMode) {
            frontLeftMotor?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            frontRightMotor?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            backLeftMotor?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            backRightMotor?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        }
        else if (!on && isBrakeMode) {
            frontLeftMotor?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
            frontRightMotor?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
            backLeftMotor?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
            backRightMotor?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
        }

        isBrakeMode = on
    }

    fun turnOnBaseLock() {
        driveControlState = DriveControlState.BASE_LOCKED

        baseLockPositions[0] = frontLeftMotor!!.currentPosition
        baseLockPositions[1] = frontLeftMotor!!.currentPosition
        baseLockPositions[2] = frontLeftMotor!!.currentPosition
        baseLockPositions[3] = frontLeftMotor!!.currentPosition

        setBrakeMode(true)
    }

    @Config
    object PathFollowingCoeffecients {
        var PID_VALUES = PIDCoefficients(0.9, 0.0, 0.0)
        var V = 1.0 //Has to be 1 over the max velocity
        var A = 0.0
    }

    fun followPath(points: Array<Waypoint>) {
        driveControlState = DriveControlState.FOLLOW_PATH

        val trajectory = Pathfinder.generate(points, trajectoryConfig)
        val modifier = TankModifier(trajectory).modify(0.5)
        //Wheel base width has to be set to the real wheel base width

        val leftTrajectory = modifier.leftTrajectory
        val rightTrajectory = modifier.rightTrajectory

        leftEncoderFollower = EncoderFollower(leftTrajectory)
        rightEncoderFollower = EncoderFollower(rightTrajectory)

        leftEncoderFollower!!.configurePIDVA(PathFollowingCoeffecients.PID_VALUES.p,
                PathFollowingCoeffecients.PID_VALUES.i, PathFollowingCoeffecients.PID_VALUES.d,
                PathFollowingCoeffecients.V, PathFollowingCoeffecients.A)
        rightEncoderFollower!!.configurePIDVA(PathFollowingCoeffecients.PID_VALUES.p,
                PathFollowingCoeffecients.PID_VALUES.i, PathFollowingCoeffecients.PID_VALUES.d,
                PathFollowingCoeffecients.V, PathFollowingCoeffecients.A)

        leftEncoderFollower!!.configureEncoder(getLeftSideEncoderPosition(), 70,
                0.1016)
        rightEncoderFollower!!.configureEncoder(getRightSideEncoderPosition(), 70,
                0.1016)
    }

    fun isFinishedFollowingPath(): Boolean =
            leftEncoderFollower!!.isFinished && rightEncoderFollower!!.isFinished

    private fun getLeftSideEncoderPosition(): Int =
            (frontLeftMotor!!.currentPosition + backLeftMotor!!.currentPosition)/2


    private fun getRightSideEncoderPosition(): Int =
            (frontRightMotor!!.currentPosition + backRightMotor!!.currentPosition)/2
}