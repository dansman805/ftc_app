package com.jdroids.team7026.ftc2017.auto.commands

import com.jdroids.team7026.ftc2017.auto.commands.commandtemplates.ParallelCommand
import com.jdroids.team7026.ftc2017.subsystems.GlyphIntake

class CloseGrabberScanVumark: ParallelCommand(listOf(SetGrabberToState(GlyphIntake.GrabberState.CLOSED), ReadVumark())){
    override fun initialize() {}

    override fun execute() {
        if (isFirstTime) {
            for (command in commands) {
                command.initialize()
            }

            isFirstTime = false
        }

        for ((i, command) in commands.withIndex()) {
            if(!command.isFinished()) {
                command.execute()
            }
            else if (!wasCommandEnded[i]) {
                command.end()
                wasCommandEnded[i] = true
            }
        }
    }

    override fun end() {}

    override fun isFinished(): Boolean {
        val finishedCommands = commands.filter {it.isFinished()}

        return commands.size == finishedCommands.size
    }
}