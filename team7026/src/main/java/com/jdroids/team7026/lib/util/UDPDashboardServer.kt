package com.jdroids.team7026.lib.util

import android.util.Log
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger


object UDPDashboardServer: Runnable {
    override fun run() {
        try {
            //Keep a socket open to listen to all the UDP trafic that is destined for this port
            val socket = DatagramSocket(8888, InetAddress.getByName("0.0.0.0"))
            socket.broadcast = true

            while (true) {
                //Receive a packet
                val receiveBuffer = ByteArray(15000, { Int -> Byte.MIN_VALUE})
                val packet = DatagramPacket(receiveBuffer, receiveBuffer.size)
                socket.receive(packet)

                //Packet received
                Log.d("Dashboard", this.javaClass.name +
                        ">>>Discovery packet received from: " + packet.address.hostAddress)
                Log.d("Dashboard", this.javaClass.name+ ">>>Packet received; data: " +
                        String(packet.data))

                //Get the message
                val message = String(packet.data).trim()

                if (message.equals("DISCOVER_DASHBOARD_REQUEST")) {
                    //Send a response
                    val sendData = "DISCOVER_DASHBOARD_RESPONSE".toByteArray()

                    val sendPacket = DatagramPacket(sendData, sendData.size, packet.address,
                            packet.port)

                    Log.d("Dashboard", this.javaClass.name + ">>>Sent packet to: " +
                    sendPacket.address.hostAddress)
                }
            }
        }
        catch (e: IOException) {
            Logger.getLogger(this.javaClass.name).log(Level.SEVERE, null, e)
        }
    }
}