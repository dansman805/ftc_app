package com.jdroids.team7026.ftc2017.auto.commands.commandtemplates

abstract class ParallelCommand(private val commands: List<Command>) : Command {
    override fun execute() {
        for (command in commands) {
            command.initialize()
        }

        for (command in commands) {
            if(!command.isFinished()) {
                command.execute()
            }
        }
    }

    override fun isFinished(): Boolean {
        val finishedCommands = commands.filter {it.isFinished()}

        return commands.size == finishedCommands.size
    }
}