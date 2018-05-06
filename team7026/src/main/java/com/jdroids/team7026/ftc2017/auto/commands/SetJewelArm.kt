package com.jdroids.team7026.ftc2017.auto.commands

import com.jdroids.team7026.ftc2017.Robot
import com.jdroids.team7026.ftc2017.auto.commands.commandtemplates.Command
import com.jdroids.team7026.ftc2017.subsystems.JewelSystem

class SetJewelArm(val jewelArmState: JewelSystem.JewelArmStates): Command {
    override fun initialize() {
        Robot.jewelSystem.setJewelArmState(jewelArmState)
    }

    override fun execute() {}

    override fun end() {}

    override fun isFinished(): Boolean {
        return Robot.jewelSystem.isJewelArmAtTarget()
    }
}