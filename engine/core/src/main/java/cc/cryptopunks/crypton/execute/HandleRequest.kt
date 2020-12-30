package cc.cryptopunks.crypton.execute

import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.Async
import cc.cryptopunks.crypton.Channels
import cc.cryptopunks.crypton.Execute
import cc.cryptopunks.crypton.Output
import cc.cryptopunks.crypton.Request
import cc.cryptopunks.crypton.Subscription
import cc.cryptopunks.crypton.Subscriptions
import cc.cryptopunks.crypton.type
import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

val handleRequest: Execute = {
    apply { handle() }
}

private suspend fun Request.handle() {
    when (action) {
        is Subscription -> dispatchSubscription(
            out = out,
            root = root,
            subscriptions = subscriptions
        )
        is Async -> dispatchAsync(
            out = out,
            root = root
        )
        else -> dispatchAction(
            out = out,
            root = root,
            channels = channels
        )
    }
}

private fun Request.dispatchSubscription(
    out: Output,
    root: CoroutineScope,
    subscriptions: Subscriptions,
) {
    when ((action as Subscription).enable) {
        false -> subscriptions[action.type]?.cancel()
        true -> subscriptions.getOrPut(action.type) {
            root.launch { invoke(out).join() }
        }
    }
}

private fun Request.dispatchAsync(
    out: Output,
    root: CoroutineScope,
) {
    root.launch { invoke(out).join() }
}

private suspend fun Request.dispatchAction(
    out: Output,
    channels: Channels,
    root: CoroutineScope,
) {
    root.launch {
        val channel = channels.getOrPut(action.channelId) {
            Channel<Request>(Channel.BUFFERED).apply {
                root.launch {
                    consumeAsFlow().collect { dispatch ->
                        dispatch.invoke(out).join()
                    }
                }
            }
        }
        channel.send(this@dispatchAction)
    }
}


private fun Request.invoke(out: Output): Job =
    scope.launch {
        val log = log
        log.builder.d {
            status = Log.Event.Status.Start.name
            message = "channelId: ${action.channelId}"
        }
//        log { Log.Status("Start") }
        try {
            scope.handle(out, action)
        } catch (e: Throwable) {
            log.builder.d {
                status = Log.Event.Status.Failed.name
                message = "$action ${action.channelId}"
                throwable = e
            }
//            log { Log.Error(e) }
            Action.Error(action, e).out()
        }
        log.builder.d {
            status = Log.Event.Status.Finished.name
        }
//        log { Log.Status("Finish") }
    }
