package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.acmerobotics.dashboard.RobotDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name="DashboardTest")
class DashboardTest : OpMode() {
    val dashboard = RobotDashboard.getInstance()

    val telemetryPacket = TelemetryPacket()

    override fun init() {

    }

    override fun loop() {
        telemetryPacket.put("x", 3.7)
        telemetryPacket.fieldOverlay().setFill("blue").fillRect(-20.0, -20.0, 40.0, 40.0)
        dashboard.sendTelemetryPacket(telemetryPacket)
    }

    override fun stop() {

    }
}