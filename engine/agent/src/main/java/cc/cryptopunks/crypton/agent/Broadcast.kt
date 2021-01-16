package cc.cryptopunks.crypton.agent

import cc.cryptopunks.crypton.Connector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

fun loopback(): Connector = loopbackSocket().connector()

fun DatagramSocket.connector() = Connector(
    input = packagesFlow(),
    output = { if (this is ByteArray) sendBroadcast(this) }
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
            emit(datagramPacket)
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
