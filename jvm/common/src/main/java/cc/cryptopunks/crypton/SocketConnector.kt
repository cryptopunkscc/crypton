package cc.cryptopunks.crypton

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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
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

private fun ByteReadChannel.flowParsedMessages(): Flow<Any> =
    flowMessages().map { it.parseMessage() }

private fun ByteReadChannel.flowMessages(): Flow<String> = flow {
    try {
        while (true) {
            val len = readInt()
            val arr = ByteArray(len)
            readFully(arr, 0, len)
            val message = arr.toString(Charsets.UTF_8)
            emit(message)
        }
    } catch (e: Throwable) {
        println("Close message flow (${e.message})")
    }
}

private fun String.parseMessage(): Any = split(":", limit = 2).let { (className, json) ->
    try {
        json.parseJson(Class.forName(PREFIX + className).kotlin as KClass<Any>)
    } catch (e: Throwable) {
        println(this)
        e.printStackTrace()
    }
}

private suspend fun ByteWriteChannel.send(any: Any) = try {
    val message = any.formatMessage()
    write(message.length) { buffer ->
        buffer.putInt(message.length)
    }
    writePacket {
        append(message)
    }
    flush()
} catch (e: IOException) {
    println("failed send $any")
    println(e.localizedMessage)
}

private const val PREFIX = "cc.cryptopunks.crypton."

private fun Any.formatMessage(): String {
    val className = this::class.java.name.removePrefix(PREFIX)
    val json = formatJson()
    return "$className:$json"
}

