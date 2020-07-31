package cc.cryptopunks.crypton.internal

import cc.cryptopunks.crypton.Connector
import cc.cryptopunks.crypton.Context
import cc.cryptopunks.crypton.action
import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import cc.cryptopunks.crypton.util.logger.coroutineLog
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach

internal fun createBinding(
    actorChannel: BroadcastChannel<Any> = BroadcastChannel(Channel.BUFFERED),
    serviceChannel: BroadcastChannel<Any> = BroadcastChannel(Channel.BUFFERED)
) = Binding(
    actorContext = connectableHolder(
        input = actorChannel,
        output = serviceChannel
    ),
    serviceContext = connectableHolder(
        input = serviceChannel,
        output = actorChannel
    )
)

private fun connectableHolder(
    input: BroadcastChannel<Any>,
    output: BroadcastChannel<Any>
) = Binding.Context(
    input = input,
    output = output,
    connector = createConnector(
        inputChannel = input.openSubscription(),
        outputChannel = output
    )
)

internal fun createConnector(
    inputChannel: ReceiveChannel<Any>,
    outputChannel: SendChannel<Any>
) = Connector(
    input = inputChannel.consumeAsFlow()
//        .onEach {
//            val action = it.unwrap()
//            coroutineLog(CoroutineLog.Action(action)).builder.i {
//                message = action.toString()
//                status = Log.Event.Status.Received.name
//            }
//        }
    ,
    output = {
//        val action = it.unwrap()
//        val log = coroutineLog(CoroutineLog.Action(action))
//        try {
//            log.builder.i {
//                message = action.toString()
//                status = Log.Event.Status.Sending.name
//            }
            outputChannel.send(it)
//        } catch (e: Throwable) {
//            log.d { ("Cannot send $it") }
//            e.printStackTrace()
//        }
    }
).logging()

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
            log.d { ("Cannot send $it") }
            e.printStackTrace()
        }
    }
)

private fun Any.unwrap() = when (this) {
    is Context -> action()
    else -> this
}
