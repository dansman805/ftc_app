package com.jdroids.team7026.ftc2017.auto.commands

import com.jdroids.team7026.ftc2017.Robot
import com.jdroids.team7026.ftc2017.auto.commands.commandtemplates.Command
import com.jdroids.team7026.lib.util.DriveSignal
import jaci.pathfinder.Waypoint

class FollowPath(private val points: Array<Waypoint>): Command {
    override fun initialize() {
        Robot.drive.followPath(points)
    }

    override fun execute() {}

    override fun end() {
        Robot.drive.setOpenLoop(DriveSignal.NEUTRAL)
    }

    override fun isFinished(): Boolean {
        return Robot.drive.isFinishedFollowingPath()
    }
}