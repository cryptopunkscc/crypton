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

private fun Action.Dispatch.dispatchSubscription(
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

private fun Action.Dispatch.dispatchAsync(
    out: Output,
    root: CoroutineScope,
) {
    root.launch { invoke(out).join() }
}

private suspend fun Action.Dispatch.dispatchAction(
    out: Output,
    channels: Channels,
    root: CoroutineScope,
) {
    root.launch {
        val channel = channels.getOrPut(action.channelId) {
            Channel<Action.Dispatch>(Channel.BUFFERED).apply {
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


private fun Action.Dispatch.invoke(out: Output): Job =
    scope.launch {
        try {
            println("start: $action ${action.channelId}")
            scope.handle(out, action)
        } catch (e: Throwable) {
            Action.Error(action, e).out()
        } finally {
            println("finish: $action")
        }
    }
