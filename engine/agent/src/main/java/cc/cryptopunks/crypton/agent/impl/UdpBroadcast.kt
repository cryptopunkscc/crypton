package cc.cryptopunks.crypton.agent.impl

import cc.cryptopunks.crypton.agent.Broadcast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress


val ports = 64000 .. 65000

fun randomPort() = ports.random()

fun tcpEndpoint(port: Int = randomPort()) = "tcp:\\localhost:$port"


fun loopback(): Broadcast = loopbackSocket().connector()

fun DatagramSocket.connector() = Broadcast(
    input = packagesFlow(),
    output = { sendBroadcast(this) }
)

const val BUFFER_SIZE = 4096
//const val BUFFER_SIZE = 4

fun DatagramSocket.packagesFlow() = flow {
    withContext(Dispatchers.IO) {
        while (true) {
            val buffer = ByteArray(BUFFER_SIZE)
            val datagramPacket = DatagramPacket(buffer, buffer.size)
            datagramPacket.offset
            receive(datagramPacket)
            emit(datagramPacket.data)
        }
    }
}

fun DatagramSocket.sendBroadcast(
    bytes: ByteArray,
) {
    val address = InetAddress.getLoopbackAddress()
    val packet = DatagramPacket(bytes, bytes.size, address, 0)
    ports.map { port ->
        packet.port = port
        send(packet)
    }
}

fun loopbackSocket() = DatagramSocket(randomPort(), InetAddress.getLoopbackAddress())
