package cc.cryptopunks.crypton.net

import cc.cryptopunks.crypton.Connector
import cc.cryptopunks.crypton.encodeScopedAction
import cc.cryptopunks.crypton.json.formatJson
import cc.cryptopunks.crypton.json.parseJson
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import java.io.IOException
import kotlin.reflect.KClass

fun Socket.connector(): Connector {
    val readChannel = openReadChannel()
    val writeChannel = openWriteChannel()
    return Connector(
        input = readChannel.flowMessages(),
        output = { writeChannel.send(this) },
        cancel = {
            readChannel.cancel()
            writeChannel.close()
        }
    )
}

private fun ByteReadChannel.flowMessages(): Flow<Any> =
    flow {
        while (true) {
            val len = readInt()
            val arr = ByteArray(len)
            readFully(arr, 0, len)
            val message = arr.toString(Charsets.UTF_8)
            emit(message)
        }
    }.scan(emptyList<String>()) { accumulator, value ->
        when (accumulator.size) {
            2 -> listOf(value)
            else -> accumulator + value
        }
    }.filter { it.size == 2 }.map { (type, message) ->
        when (type) {
            "s" -> message
            else -> message.parseMessage(type)
        }
    }

private fun String.parseMessage(type: String): Any = try {
    parseJson(Class.forName(PREFIX + type).kotlin as KClass<Any>)
} catch (e: Throwable) {
    println(this)
    e.printStackTrace()
    this
}

private suspend fun ByteWriteChannel.send(any: Any) = try {
    any.encodeScopedAction().forEach { chunk ->
        when (chunk) {
            is String -> send("s", chunk)
            else -> send(chunk.type(), chunk.formatJson())
        }
    }
    flush()
} catch (e: IOException) {
    println("failed send $any")
    println(e.localizedMessage)
}

private suspend fun ByteWriteChannel.send(type: String, message: String) {
    send(type.encodeToByteArray())
    send(message.encodeToByteArray())
}

private suspend fun ByteWriteChannel.send(packet: ByteArray) {
    write(packet.size) { buffer ->
        buffer.putInt(packet.size)
    }
    writePacket {
        writeFully(packet)
    }
}

private const val PREFIX = "cc.cryptopunks.crypton."

private fun Any.type() = javaClass.name.removePrefix(PREFIX)
