package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow

internal class ConnectableBinding : Connector, Connectable.Binding {

    private val log = typedLog()

    private val channel = BroadcastChannel<Any>(Channel.BUFFERED)

    private val reserved = Channel<ReceiveChannel<Any>>(Channel.BUFFERED)

    private val serviceJobs = mutableMapOf<Connectable, Job>()

    override val services get() = serviceJobs.keys

    override val output: suspend (Any) -> Unit = channel::send

    override val input: Flow<Any> get() = channel.openSubscription().consumeAsFlow()

    override fun reserve(count: Int) = repeat(count) {
        reserved.offer(channel.openSubscription())
    }

    override suspend fun cancel() {
        channel.close()
        serviceJobs.values.forEach { it.cancel() }
        serviceJobs.clear()
        reserved.cancel()
    }

    override operator fun plus(service: Connectable?): Boolean {
        return service.ifNotRunning {
            log.d("Connecting $service")
            serviceJobs[it] = connect()
        }
    }

    override fun minus(service: Connectable) =
        serviceJobs.remove(service)?.cancel() != null

    override operator fun plusAssign(service: Connectable) {
        service.ifNotRunning {
            log.d("Connecting $service")
            serviceJobs[it] = ReservedConnector().connect()
        }
    }

    private fun Connectable?.ifNotRunning(block: Connectable.(Connectable) -> Unit): Boolean = this
        ?.takeIf {
            this !in serviceJobs
        }?.let {
            block.invoke(it, it)
        } != null

    private inner class ReservedConnector : Connector {
        override val input: Flow<Any> = reserved.poll()
            ?.consumeAsFlow()
            ?: this@ConnectableBinding.input

        override val output: suspend (Any) -> Unit = channel::send
    }
}
