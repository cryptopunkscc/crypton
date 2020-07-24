package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.OpenStore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext

interface Subscription {
    val enable: Boolean
}

interface Async

fun Connectable.dispatch(
    vararg args: Any,
    output: suspend (Any) -> Unit = {}
) = connector(*args, output).connect()

interface Connectable : CoroutineScope {
    interface Binding {
        class Store : OpenStore<List<WeakReference<out Binding>>>(emptyList())

        val services: Set<Connectable>
        suspend fun cancel(cause: CancellationException? = null)
        operator fun plus(service: Connectable?): Binding
        operator fun minus(service: Connectable?): Binding
        fun send(any: Any) = Unit
        operator fun Connectable.unaryPlus() = plus(this)
        operator fun Connectable.unaryMinus() = minus(this)
    }

    val id: Any get() = this::class.java.simpleName

    fun Connector.connect(): Job = launch { }
    fun Connector.connect(service: Connectable): Job = launch {
        service.run { connect() }.join()
    }
}

inline fun <reified T : Connectable> Connectable.Binding.minus() =
    services.filterIsInstance<T>().forEach { minus(it) }

interface Actor : Connectable {
    companion object {
        val Empty = object : Actor {
            override val coroutineContext get() = Dispatchers.Unconfined
        }
    }
}

fun actor(
    context: CoroutineContext = Dispatchers.Unconfined,
    onConnect: suspend CoroutineScope.(Connector) -> Unit
) = object : Actor {
    override val coroutineContext = SupervisorJob() + context
    override fun Connector.connect(): Job = launch { onConnect(this@connect) }
}
