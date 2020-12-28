package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import cc.cryptopunks.crypton.util.logger.coroutineLog
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach

typealias Output = suspend Any.() -> Unit

data class Connector(
    val input: Flow<Any>,
    val close: () -> Unit = {},
    val output: suspend (Any) -> Unit = {}
) {
    suspend fun Any.out() = output(this)
}

fun Flow<Any>.connector(
    output: suspend (Any) -> Unit = {}
) = Connector(
    input = this,
    output = output
)

fun Any.connector(
    output: suspend (Any) -> Unit = {}
) = Connector(
    input = flowOf(this),
    output = output
)

fun connector(
    vararg args: Any,
    output: suspend (Any) -> Unit = {}
) = Connector(
    input = args.toList().asFlow(),
    output = output
)

fun connector(
    inputChannel: ReceiveChannel<Any>,
    outputChannel: SendChannel<Any>
) = Connector(
    input = inputChannel.consumeAsFlow(),
    output = { outputChannel.send(it) },
//    close = { inputChannel.cancel() }
)

fun Connector.logging() = copy(
    input = input.onEach {
        val action = it.unwrap()
        coroutineLog(CoroutineLog.Action(action)).builder.i {
            message = action.toString()
            status = Log.Event.Status.Received.name
        }
    },
    output = {
        val action = it.unwrap()
        val log = coroutineLog(CoroutineLog.Action(action))
        log.builder.i {
            message = action.toString()
            status = Log.Event.Status.Sending.name
        }
        try {
            output(it)
        } catch (e: Throwable) {
            log.builder.e {
                throwable = e
                message = "Cannot send $it"
            }
        }
    }
)

private fun Any.unwrap() = when (this) {
    is Context -> action()
    else -> this
}

fun Connectable.connector(): Connector =
    connectorAdapter().run {
        val job = first.connect()
        second.copy(
            close = {
                job.cancel()
                first.close()
                second.close()
            }
        )
    }

fun connectorAdapter(
    first: BroadcastChannel<Any> = BroadcastChannel(Channel.BUFFERED),
    second: BroadcastChannel<Any> = BroadcastChannel(Channel.BUFFERED)
): Pair<Connector, Connector> = connector(
    inputChannel = first.openSubscription(),
    outputChannel = second
) to connector(
    inputChannel = second.openSubscription(),
    outputChannel = first
)
