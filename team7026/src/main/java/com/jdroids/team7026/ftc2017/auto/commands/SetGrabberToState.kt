package com.jdroids.team7026.ftc2017.auto.commands

import com.jdroids.team7026.ftc2017.Robot
import com.jdroids.team7026.ftc2017.auto.commands.commandtemplates.Command
import com.jdroids.team7026.ftc2017.subsystems.GlyphIntake

class SetGrabberToState(val targetState: GlyphIntake.GrabberState) : Command {
    init {
        requireNotNull(Robot.glyphIntake)
    }

    private var isFinished = false

    override fun initialize() {
        Robot.glyphIntake.setBothGrabberStates(targetState)
        isFinished = true
    }

    override fun execute() {
        //Nothing to do here
    }

    override fun end() {
        //Nothing to do here
    }

    override fun isFinished(): Boolean {
        return isFinished
    }
}