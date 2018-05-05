package com.jdroids.team7026.ftc2017.subsystems

import com.qualcomm.robotcore.eventloop.opmode.OpMode

abstract class Subsystem {
    var opMode: OpMode? = null

    abstract fun start(opMode: OpMode)

    abstract fun outputToTelemetry()

    abstract fun stop()

    abstract fun zeroSensors()
}