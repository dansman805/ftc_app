package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.RobotDashboard
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.PIDCoefficients
import com.qualcomm.robotcore.util.ElapsedTime

@Autonomous(name="DashboardTest")
class DashboardTest: OpMode(){
    private val dashboard: RobotDashboard = RobotDashboard.getInstance()
    val telemetryPacket = TelemetryPacket()
    val runtime = ElapsedTime()

    override fun init() {
        telemetryPacket.put("Program", "Initialized")
        dashboard.sendTelemetryPacket(telemetryPacket)
    }

    override fun start() {
        telemetryPacket.put("Program", "Started")
        dashboard.sendTelemetryPacket(telemetryPacket)
        runtime.reset()
    }

    @Config
    class MagicValueTest {
        companion object {
            @JvmField var magicValue = 0.0
            @JvmField var testPIDCoefficients = PIDCoefficients(0.0, 0.0,0.0)
        }
    }

    override fun loop() {
        telemetryPacket.put("Magic Value", MagicValueTest.magicValue)
        telemetryPacket.put("PID Coefficients", "P: " + MagicValueTest.testPIDCoefficients.p +
                " I: " + MagicValueTest.testPIDCoefficients.i + " D:" +
                MagicValueTest.testPIDCoefficients.d)
        telemetryPacket.put("Time between Loops", runtime.milliseconds())
        runtime.reset()
        dashboard.sendTelemetryPacket(telemetryPacket)
    }
}