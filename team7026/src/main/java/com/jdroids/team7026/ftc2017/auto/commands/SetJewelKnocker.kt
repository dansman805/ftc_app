package com.jdroids.team7026.ftc2017.auto.commands

import com.jdroids.team7026.ftc2017.Robot
import com.jdroids.team7026.ftc2017.auto.commands.commandtemplates.Command
import com.jdroids.team7026.ftc2017.subsystems.JewelSystem

class SetJewelKnocker(val jewelKnockerState: JewelSystem.JewelKnockerStates): Command {
    override fun initialize() {
        Robot.jewelSystem.setJewelKnockerState(jewelKnockerState)
    }

    override fun execute() {}

    override fun end() {}

    override fun isFinished(): Boolean {
        return Robot.jewelSystem.isJewelArmAtTarget()
    }
}