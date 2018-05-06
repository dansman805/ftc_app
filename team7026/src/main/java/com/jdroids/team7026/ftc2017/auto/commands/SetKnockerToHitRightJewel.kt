package com.jdroids.team7026.ftc2017.auto.commands

import com.jdroids.team7026.ftc2017.Robot
import com.jdroids.team7026.ftc2017.auto.commands.commandtemplates.Command
import com.jdroids.team7026.ftc2017.subsystems.JewelSystem

class SetKnockerToHitRightJewel(val allianceColor: DetectJewel.Color): Command {

    override fun initialize() {
        val targetState: JewelSystem.JewelKnockerStates
        if (allianceColor == Robot.globalValues.jewelOnLeft) {
            targetState = JewelSystem.JewelKnockerStates.KNOCK_RIGHT_JEWEL
        }
        else if (allianceColor != Robot.globalValues.jewelOnLeft){
            targetState = JewelSystem.JewelKnockerStates.KNOCK_LEFT_JEWEL
        }
        else {
            targetState = JewelSystem.JewelKnockerStates.CENTERED
        }

        Robot.jewelSystem.setJewelKnockerState(targetState)
    }

    override fun execute() {}

    override fun end() {}

    override fun isFinished(): Boolean {
        return Robot.jewelSystem.isKnockerAtTarget()
    }
}