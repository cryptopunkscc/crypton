package cc.cryptopunks.crypton.net

import cc.cryptopunks.crypton.Connector
import cc.cryptopunks.crypton.encodeContext
import cc.cryptopunks.crypton.json.formatJson
import cc.cryptopunks.crypton.json.parseJson
import cc.cryptopunks.crypton.util.TypedLog
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.cancel
import io.ktor.utils.io.close
import io.ktor.utils.io.writePacket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import java.io.IOException
import kotlin.reflect.KClass


fun Socket.connector(log: TypedLog): Connector = let {
    val readChannel = openReadChannel()
    val writeChannel = openWriteChannel()
    Connector(
        input = readChannel.flowParsedMessages(),
        output = {
            log.d("Sending $it")
            writeChannel.send(it)
        },
        close = {
            writeChannel.close()
            readChannel.cancel()
            dispose()
        }
    )
}

private fun ByteReadChannel.flowParsedMessages(): Flow<Any> = flowMessages()

private fun ByteReadChannel.flowMessages(): Flow<Any> = flow {
    try {
        while (true) {
            val len = readInt()
            val arr = ByteArray(len)
            readFully(arr, 0, len)
            val message = arr.toString(Charsets.UTF_8)
            emit(message)
        }
    } catch (e: Throwable) {
        e.printStackTrace()
        println("Close message flow (${e.message})")
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
    any.encodeContext().also {
        println("Sending encoded $it")
    }.forEach { chunk ->
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
    send(type)
    send(message)
}

private suspend fun ByteWriteChannel.send(packet: String) {
    write(packet.length) { buffer ->
        buffer.putInt(packet.length)
    }
    writePacket {
        append(packet)
    }
}

private const val PREFIX = "cc.cryptopunks.crypton."

private fun Any.type() = javaClass.name.removePrefix(PREFIX)
