package com.jdroids.team7026.ftc2017.auto.commands

import com.jdroids.externalresources.ClosableVuforiaLocalizer
import com.jdroids.team7026.ftc2017.auto.commands.commandtemplates.Command
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark
import com.jdroids.team7026.ftc2017.Robot
import com.qualcomm.ftcrobotcontroller.R.id.cameraMonitorViewId
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables

class ReadVumark: Command{
    private val timer = ElapsedTime()

    private var vuforia: ClosableVuforiaLocalizer? = null

    private var relicTrackables: VuforiaTrackables? = null
    private var relicTemplate: VuforiaTrackable? = null

    private var isFinished = false

    override fun initialize() {
        val cameraMonitorViewID = Robot.opMode?.hardwareMap?.appContext?.resources?.getIdentifier(
                "cameraMonitorViewId",
                "id", Robot.opMode?.hardwareMap?.appContext?.packageName)

        val parameters: VuforiaLocalizer.Parameters = VuforiaLocalizer.Parameters(cameraMonitorViewId)

        parameters.vuforiaLicenseKey = " AZcIMlr/////AAAAGe1W/L9P20hXupxJsIH5bIMDl46JPwjrX2kI+L6" +
                "+tigIG9bhthzvrEWVBni6g4Jkvs76N/hIT0bFun78pnNDqkG3ZP24XLj45VHA2rYKp8UDww/vfy8xrt" +
                "vHxedihdX1A2vMWg8Ub8tLjBMgEAqcAYYUMwPRQfI61KQmXvAJBV79XtQughxCh/fbrtoux6WV6HHs8" +
                "OydP7kPUaUU3f0z5ZOF/TUvcqFFotqnLg/KwXMxxrouRyDGCIbpbP7cYabiR7ShIGvrYoRKtbpwxS3W" +
                "LSjjTd7ynvoidYipWZ60e6t+wUCzdXahS8g0veYuTQ+vwBqljhtLUWnCUjbJh2jocjxV9kLGgqlPFCm" +
                "LHZyurYkX"

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT

        this.vuforia = ClosableVuforiaLocalizer(parameters)
        timer.reset()

        relicTrackables = this.vuforia?.loadTrackablesFromAsset("RelicVuMark")
        relicTemplate = relicTrackables?.get(0)
        relicTemplate?.name = "relicVuMarkTemplate" //For debug purposes

        relicTrackables?.activate()
    }

    override fun execute() {
        if (timer.milliseconds() > 3000) {
            isFinished = true
        }
        else {
            Robot.globalValues.vumark = RelicRecoveryVuMark.from(relicTemplate)
            if(Robot.globalValues.vumark != RelicRecoveryVuMark.UNKNOWN) {
                isFinished = true
            }
        }
    }

    override fun end() {
        vuforia?.close()
    }

    override fun isFinished(): Boolean {
        return isFinished
    }
}