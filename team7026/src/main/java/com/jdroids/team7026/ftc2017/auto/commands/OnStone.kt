package com.jdroids.team7026.ftc2017.auto.commands

import com.jdroids.team7026.ftc2017.auto.commands.commandtemplates.SequentialCommand

class OnStone(private val allianceColor: DetectJewel.Color): SequentialCommand(
        listOf(CloseGrabberScanVumark(), KnockJewel(allianceColor))) {
    override fun initialize() {}

    override fun execute() {
        if(isFirstTimeRunningCommand) {
            commands[currentCommand].initialize()
            isFirstTimeRunningCommand = false
        }

        commands[currentCommand].execute()

        if(commands[currentCommand].isFinished()) {
            commands[currentCommand].end()
            currentCommand++
        }
    }

    override fun end() {}

    override fun isFinished(): Boolean {
        return currentCommand == commands.size
    }
}