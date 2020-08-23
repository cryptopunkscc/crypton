package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.OpenStore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

fun Connectable.dispatch(
    vararg args: Any,
    output: suspend (Any) -> Unit = {}
) = connector(*args, output).connect()

interface Connectable : CoroutineScope {
    interface Binding {
        class Store : OpenStore<List<WeakReference<out Binding>>>(emptyList())

        val services: Set<Connectable>
        fun send(any: Any) = Unit
        suspend fun cancel(cause: CancellationException? = null)
        operator fun plus(service: Connectable?): Binding
        operator fun minus(service: Connectable?): Binding
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
