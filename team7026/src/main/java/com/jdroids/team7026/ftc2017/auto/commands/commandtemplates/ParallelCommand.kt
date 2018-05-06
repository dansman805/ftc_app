package com.jdroids.team7026.ftc2017.auto.commands.commandtemplates

abstract class ParallelCommand(val commands: List<Command>) : Command {
    var isFirstTime = true
    var wasCommandEnded = Array(commands.size, { Int -> false})

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

    override fun isFinished(): Boolean {
        val finishedCommands = commands.filter {it.isFinished()}

        return commands.size == finishedCommands.size
    }
}