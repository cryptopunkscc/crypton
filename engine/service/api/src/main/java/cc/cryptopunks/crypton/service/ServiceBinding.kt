@file:Suppress("FunctionName")

package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Service
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ServiceBinding : Service.Connector by ServiceConnector() {

    private val serviceJobs = mutableMapOf<Service, Job>()

    val services get() = serviceJobs.keys

    var slot1 by Slot()

    var slot2 by Slot()

    operator fun plus(service: Service?): Boolean = service?.run {
        takeIf {
            service !in serviceJobs
        }?.let {
            serviceJobs[service] = connect()
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

