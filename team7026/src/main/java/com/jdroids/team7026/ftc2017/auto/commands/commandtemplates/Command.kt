package com.jdroids.team7026.ftc2017.auto.commands.commandtemplates

interface Command {
    fun initialize()

    fun execute()

    fun end()

    fun isFinished() : Boolean
}