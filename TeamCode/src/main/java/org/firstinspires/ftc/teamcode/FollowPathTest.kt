package org.firstinspires.ftc.teamcode

import com.jdroids.team7026.ftc2017.Robot
import com.jdroids.team7026.ftc2017.auto.commands.FollowPath
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import jaci.pathfinder.Pathfinder
import jaci.pathfinder.Waypoint

@Autonomous(name="FollowPathTest")
class FollowPathTest: OpMode() {
    private val followPath = FollowPath(arrayOf(Waypoint(0.0, 0.0,
            Pathfinder.d2r(0.0)), Waypoint(0.1, 0.2, Pathfinder.d2r(45.0)),
            Waypoint(0.3, 0.4, Pathfinder.d2r(90.0))))

    override fun init() {
        Robot.start(this)
    }

    override fun start() {
        Robot.ControlLoop.onStart()
        followPath.initialize()
    }

    override fun loop() {
        followPath.execute()
        Robot.ControlLoop.onLoop()
        Robot.outputToTelemetry()

        if(followPath.isFinished()) {
            stop()
        }
    }

    override fun stop() {
        followPath.end()
        Robot.ControlLoop.onStop()
    }
}