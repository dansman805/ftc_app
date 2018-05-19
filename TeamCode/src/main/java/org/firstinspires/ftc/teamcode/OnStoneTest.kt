package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.jdroids.team7026.ftc2017.Robot
import com.jdroids.team7026.ftc2017.auto.commands.DetectJewel
import com.jdroids.team7026.ftc2017.auto.commands.OnStone
import com.qualcomm.robotcore.eventloop.opmode.Autonomous

@Autonomous(name="OnStoneTest")
class OnStoneTest: OpMode() {
    private val onStone = OnStone(DetectJewel.Color.RED)

    override fun init() {
        Robot.start(this)
    }

    override fun start() {
        Robot.ControlLoop.onStart()
        onStone.initialize()
    }

    override fun loop() {
        onStone.execute()
        Robot.ControlLoop.onLoop()
        Robot.outputToTelemetry()

        if(onStone.isFinished()) {
            stop()
        }
    }

    override fun stop() {
        onStone.end()
        Robot.ControlLoop.onStop()
    }
}