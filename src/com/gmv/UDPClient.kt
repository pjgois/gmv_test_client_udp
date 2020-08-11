package com.gmv

import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.text.MessageFormat

const val STX: Byte = 0x02
//const val RS: Byte = 0x1E
const val ETX: Byte = 0x03

class UDPClient {

    private val socket: DatagramSocket = DatagramSocket()
    private val address: InetAddress = InetAddress.getByName("localhost")
    private lateinit var buf: ByteArray

    fun sendBytes() {

        buf = msgByteArray()
        var packet = DatagramPacket(buf, buf.size, address, 4445)
        socket.send(packet)
        packet = DatagramPacket(buf, buf.size)
        socket.receive(packet)
        println(MessageFormat.format("Received message: {0}", String(packet.data, 0, packet.length)))
        close()
    }

    private fun close() {
        socket.close()
    }

    private fun msgByteArray(): ByteArray {

        val lat: Long = 38753218
        val lon: Long = -9167966
        try {
            ByteArrayOutputStream().use { byteArrayOutputStream ->
                DataOutputStream(byteArrayOutputStream).use { dataOutputStream ->
                    dataOutputStream.write(STX.toInt())
                    dataOutputStream.writeLong(lat)
//                    dataOutputStream.write(RS.toInt())
                    dataOutputStream.writeLong(lon)
                    dataOutputStream.write(ETX.toInt())
                    dataOutputStream.flush()
                }
                return byteArrayOutputStream.toByteArray()
            }
        } catch (e: IOException) {
            println(MessageFormat.format("Error while creating message: {0}", e.message))
            return ByteArray(0)
        }
    }
}