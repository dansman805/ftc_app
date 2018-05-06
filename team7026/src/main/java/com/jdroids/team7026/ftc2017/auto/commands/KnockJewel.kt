package com.jdroids.team7026.ftc2017.auto.commands

import com.jdroids.team7026.ftc2017.auto.commands.commandtemplates.ParallelCommand
import com.jdroids.team7026.ftc2017.subsystems.JewelSystem

class KnockJewel(private val allianceColor: DetectJewel.Color): ParallelCommand(listOf(DetectJewel(),
        SetJewelKnocker(JewelSystem.JewelKnockerStates.CENTERED),
        SetJewelArm(JewelSystem.JewelArmStates.DOWN), KnockJewel(allianceColor),
        SetJewelKnocker(JewelSystem.JewelKnockerStates.CENTERED),
        SetJewelArm(JewelSystem.JewelArmStates.UP),
        SetJewelKnocker(JewelSystem.JewelKnockerStates.KNOCK_LEFT_JEWEL))) {

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