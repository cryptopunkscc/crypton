package cc.cryptopunks.crypton.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext

private val StoreDispatcher = newSingleThreadContext("Store")

open class Store<T>(
    initial: T,
    val dispatcher: CoroutineDispatcher = StoreDispatcher
) {
    private val channel = ConflatedBroadcastChannel(initial)
    fun get() = channel.value
    operator fun invoke() = get()
    operator fun invoke(reduce: T.() -> T?): T? =
        channel.value.reduce()?.also {
            channel.offer(it)
        }
    suspend infix fun reduce(f: suspend T.() -> T?): T? = withContext(dispatcher) {
        channel.value.f()?.also {
            channel.send(it)
        }
    }

    fun changesFlow() = channel.asFlow()
}

typealias OpenStore<T> = Store<T>

suspend fun <T> Store<List<T>>.pop() = get().also { reduce { dropLast(1) } }.lastOrNull()

suspend fun <T> Store<List<T>>.put(item: T) = reduce { plus(item) }

fun <T> Store<List<T>>.top() = get().lastOrNull()
