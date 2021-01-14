package cc.cryptopunks.crypton.execute

import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.Async
import cc.cryptopunks.crypton.Channels
import cc.cryptopunks.crypton.Execute
import cc.cryptopunks.crypton.Output
import cc.cryptopunks.crypton.Request
import cc.cryptopunks.crypton.RequestLog
import cc.cryptopunks.crypton.Subscription
import cc.cryptopunks.crypton.Subscriptions
import cc.cryptopunks.crypton.logv2.Log
import cc.cryptopunks.crypton.logv2.LogElement
import cc.cryptopunks.crypton.logv2.d
import cc.cryptopunks.crypton.logv2.e
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
        channels.getOrPut(action.channelId) {
            Channel<Request>(Channel.BUFFERED).apply {
                root.launch {
                    consumeAsFlow().collect { dispatch ->
                        dispatch.invoke(out).join()
                    }
                }
            }
        }.send(this@dispatchAction)
    }
}


private fun Request.invoke(out: Output): Job =
    launch(log.map(Unit)) {
        log.d { RequestLog.Event.Start }
        try {
            copy(scope = this).handle(out, action)
        } catch (e: Throwable) {
            log.e { e }
            Action.Error(action, e).out()
        }
        log.d { RequestLog.Event.Finish }
    }

private fun <S> Log<in Any, Any>.map(scope: S) =
    LogElement<S, Any> { level, build -> invoke(level) { scope.build() } }
