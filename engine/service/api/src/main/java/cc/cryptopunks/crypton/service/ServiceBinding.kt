@file:Suppress("FunctionName")

package cc.cryptopunks.crypton.service

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
) : Service.Connector by provider {

    private val log = typedLog()

    private val channel = BroadcastChannel<Any>(Channel.BUFFERED)

    private val reserved = Channel<ReceiveChannel<Any>>(Channel.BUFFERED)

    private val serviceJobs = mutableMapOf<Service, Job>()

    override val output: suspend (Any) -> Unit = channel::send

    override val input: Flow<Any>
        get() = (reserved.poll() ?: channel.openSubscription())
            .consumeAsFlow()

    fun reserve(count: Int) {
        (1..count)
            .map { channel.openSubscription() }
            .forEach { reserved.offer(it) }
    }

    operator fun plus(service: Service?): Boolean {
        return service.ifNotRunning {
            log.d("connecting $service")
            serviceJobs[it] = connect()
        }
    }

    operator fun plusAssign(service: Service) {
        service.ifNotRunning {
            log.d("connecting $service")
            serviceJobs[it] = obtainConnector().connect()
        }
    }

    private fun Service?.ifNotRunning(block: Service.(Service) -> Unit): Boolean = this
        ?.takeIf {
            this !in serviceJobs
        }?.let {
            block.invoke(it, it)
        }!= null

    private fun obtainConnector() = Connector(
        output = output,
        input = reserved.poll()
            ?.consumeAsFlow()
            ?: throw Exception("No reserved subscriptions")
    )

    private inner class Connector(
        override val input: Flow<Any>,
        override val output: suspend (Any) -> Unit
    ) : Service.Connector
}


class ServiceBinding internal constructor(
    private val provider: ServiceConnectorProvider = ServiceConnectorProvider()
) : Service.Connector by provider {

    private val log = typedLog()

    private val serviceJobs = mutableMapOf<Service, Job>()

    val services get() = serviceJobs.keys

    var slot1 by Slot()

    var slot2 by Slot()

    operator fun plus(service: Service?): Boolean = service?.run {
        takeIf {
            service !in serviceJobs
        }?.let {
            log.d("connecting $service")
            serviceJobs[service] = provider.openSubscription().connect()
        }
    } != null

    operator fun minus(service: Service?) =
        serviceJobs.remove(service)?.cancel() != null


    inline fun <reified T : Service> minus() =
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

    inner class Slot internal constructor() : ReadWriteProperty<Any, Service?> {
        private var value: Service? = null
        override fun getValue(thisRef: Any, property: KProperty<*>) = value
        override fun setValue(thisRef: Any, property: KProperty<*>, value: Service?) {
            minus(this.value)
            plus(value)
            this.value = value
        }
    }
}

