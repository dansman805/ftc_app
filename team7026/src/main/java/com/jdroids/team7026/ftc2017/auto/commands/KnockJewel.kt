package com.jdroids.team7026.ftc2017.auto.commands

import com.jdroids.team7026.ftc2017.auto.commands.commandtemplates.SequentialCommand
import com.jdroids.team7026.ftc2017.subsystems.JewelSystem

class KnockJewel(private val allianceColor: DetectJewel.Color): SequentialCommand(listOf(DetectJewel(),
        SetJewelKnocker(JewelSystem.JewelKnockerStates.CENTERED),
        SetJewelArm(JewelSystem.JewelArmStates.DOWN), KnockJewel(allianceColor),
        SetJewelKnocker(JewelSystem.JewelKnockerStates.CENTERED),
        SetJewelArm(JewelSystem.JewelArmStates.UP),
        SetJewelKnocker(JewelSystem.JewelKnockerStates.KNOCK_LEFT_JEWEL))) {

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