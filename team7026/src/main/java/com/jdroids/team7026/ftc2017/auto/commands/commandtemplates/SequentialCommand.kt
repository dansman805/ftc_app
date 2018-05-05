package com.jdroids.team7026.ftc2017.auto.commands.commandtemplates

abstract class SequentialCommand(private val commands: List<Command>) : Command {
    override fun execute() {
        for (command in commands) {
            command.initialize()

            while (!command.isFinished()) {
                command.execute()
            }

            command.end()
        }
    }

    override fun isFinished(): Boolean {
        val finishedCommands = commands.filter {it.isFinished()}

        return commands.size == finishedCommands.size
    }
}