@file:Suppress("FunctionName")

package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Service
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ServiceBinding {

    private val serviceJobs = mutableMapOf<Service, Job>()

    private val connector = ServiceConnector()

    val services get() = serviceJobs.keys

    var slot1 by Slot()

    var slot2 by Slot()

    operator fun plus(service: Service?): Boolean = service?.run {
        takeIf {
            service !in serviceJobs
        }?.let {
            serviceJobs[service] = connector.connect()
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

    fun snapshot() = Snapshot(slot1, slot2)

    class Snapshot internal constructor(
        val left: Service?,
        val right: Service?
    ) {
        val isVisible get() = left != null
        val isAttached get() = right != null
        inline fun <reified T : Any> isTypeOf() = left is T
    }

    inner class Slot internal constructor(): ReadWriteProperty<Any, Service?> {
        private var value: Service? = null
        override fun getValue(thisRef: Any, property: KProperty<*>) = value
        override fun setValue(thisRef: Any, property: KProperty<*>, value: Service?) {
            minus(this.value)
            plus(value)
            this.value = value
        }
    }
}

