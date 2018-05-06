package com.jdroids.team7026.ftc2017.auto.commands.commandtemplates

import com.qualcomm.robotcore.robot.Robot

abstract class SequentialCommand(val commands: List<Command>) : Command {
    var currentCommand = 0
    var isFirstTimeRunningCommand = true

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

    override fun isFinished(): Boolean {
        return currentCommand == commands.size
    }
}