package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.json.formatJson
import cc.cryptopunks.crypton.json.parseJson
import cc.cryptopunks.crypton.util.TypedLog
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class SocketConnector(
    val log: TypedLog,
    val inputChannel: ByteReadChannel,
    val outputChannel: ByteWriteChannel
) : Connector {

    private val channel = BroadcastChannel<Any>(Channel.BUFFERED)

    suspend fun init() = coroutineScope {
        launch(Dispatchers.IO) {
            inputChannel.flowParsedMessages().collect {
                channel.send(it)
            }
        }
    }

    override val input: Flow<Any> get() = channel.asFlow()

    override val output: suspend (Any) -> Unit = { any ->
        send(any)
    }
}


fun ByteReadChannel.flowParsedMessages(): Flow<Any> =
    flowMessages().map { it.parseMessage() }

private fun ByteReadChannel.flowMessages(): Flow<String> = flow {
    while (true) {
        val len = readInt()
        val arr = ByteArray(len)
        readFully(arr, 0, len)
        val message = arr.toString(Charsets.UTF_8)
        emit(message)
    }
}

private fun String.parseMessage(): Any = split(":", limit = 2).let { (className, json) ->
    json.parseJson(Class.forName(PREFIX+className).kotlin as KClass<Any>)
}

suspend fun SocketConnector.send(vararg any: Any) {
    any.forEach { send(it) }
}

private suspend fun SocketConnector.send(any: Any) {
    val message = any.formatMessage()
    log.d("Sending $message")
    outputChannel.write { buffer ->
        buffer.putInt(message.length)
        buffer.put(message.toByteArray())
    }
}

private const val PREFIX = "cc.cryptopunks.crypton."

private fun Any.formatMessage(): String {
    val className = this::class.java.name.removePrefix(PREFIX)
    val json = formatJson()
    return "$className:$json"
}

