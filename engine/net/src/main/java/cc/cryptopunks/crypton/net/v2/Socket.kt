package cc.cryptopunks.crypton.net.v2

import cc.cryptopunks.crypton.Connector
import cc.cryptopunks.crypton.TypedConnector
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.nio.ByteBuffer

fun Socket.connector(): TypedConnector<ByteArray> {
    val readChannel = openReadChannel()
    val writeChannel = openWriteChannel()
    return TypedConnector(
        input = readChannel.flowMessages(),
        output = { writeChannel.writeFully(this) },
        cancel = {
            readChannel.cancel()
            writeChannel.close()
        }
    )
}


private fun ByteReadChannel.flowMessages(): Flow<ByteArray> =
    flow {
        val buffer = ByteBuffer.allocate(64)
        var len = 0
        while (true) {
            readFully(buffer)
            len = readAvailable(buffer)
            buffer.limit()
            buffer.array().copyOf(len)
        }
    }
