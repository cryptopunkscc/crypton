@file:Suppress("FunctionName")

package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import java.lang.Exception
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ServiceBinding2 internal constructor(
    private val provider: ServiceConnectorProvider = ServiceConnectorProvider()
) : Connector by provider {

    private val log = typedLog()

    private val channel = BroadcastChannel<Any>(Channel.BUFFERED)

    private val reserved = Channel<ReceiveChannel<Any>>(Channel.BUFFERED)

    private val serviceJobs = mutableMapOf<Service, Job>()

    override val output: suspend (Any) -> Unit = channel::send

    override val input: Flow<Any>
        get() = (reserved.poll() ?: channel.openSubscription()).consumeAsFlow()

    fun reserve(count: Int) = repeat(count) {
        reserved.offer(channel.openSubscription())
    }

    operator fun plus(service: Connectable?): Boolean {
        return service.ifNotRunning {
            log.d("connecting $service")
            serviceJobs[it] = connect()
        }
    }

    operator fun plusAssign(service: Connectable) {
        service.ifNotRunning {
            log.d("connecting $service")
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
            ?: throw Exception("No reserved subscriptions")

        override val output: suspend (Any) -> Unit = channel::send
    }
}


class ServiceBinding internal constructor(
    private val provider: ServiceConnectorProvider = ServiceConnectorProvider()
) : Connector by provider {

    private val log = typedLog()

    private val serviceJobs = mutableMapOf<Connectable, Job>()

    val services get() = serviceJobs.keys

    var slot1 by Slot()

    var slot2 by Slot()

    operator fun plus(service: Connectable?): Boolean = service?.run {
        takeIf {
            service !in serviceJobs
        }?.let {
            log.d("connecting $service")
            serviceJobs[service] = provider.openSubscription().connect()
        }
    } != null

    operator fun minus(service: Connectable?) =
        serviceJobs.remove(service)?.cancel() != null


    inline fun <reified T : Connectable> minus() =
        services.filterIsInstance<T>().map {
            minus(it)
        }.isNotEmpty()

    fun cancel() {
        serviceJobs.keys.forEach { it.cancel() }
        clear()
    }

    fun clear() {
        slot1 = null
        slot2 = null
        serviceJobs.run {
            values.forEach { it.cancel() }
            clear()
        }
    }

    inner class Slot internal constructor() : ReadWriteProperty<Any, Connectable?> {
        private var value: Connectable? = null
        override fun getValue(thisRef: Any, property: KProperty<*>) = value
        override fun setValue(thisRef: Any, property: KProperty<*>, value: Connectable?) {
            minus(this.value)
            plus(value)
            this.value = value
        }
    }
}

