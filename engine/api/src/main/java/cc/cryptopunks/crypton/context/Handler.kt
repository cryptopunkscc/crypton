package cc.cryptopunks.crypton.context

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.reflect.KClass


fun <T : Any> CoroutineScope.handle(handle: suspend T.(ConnectorOutput) -> Unit): Handle<T> = Handler(this, handle)

interface Handle<T> {
    operator fun invoke(arg: T, output: ConnectorOutput = {}): Job
}

private class Handler<T>(
    val scope: CoroutineScope,
    val handle: suspend T.(ConnectorOutput) -> Unit
) : Handle<T> {
    override fun invoke(arg: T, output: ConnectorOutput) = scope.launch { arg.handle(output) }
}

class CompoundHandler(
    private val registry: HandlerRegistry
) : Handle<Any> {
    override fun invoke(arg: Any, output: ConnectorOutput): Job =
        registry.dispatch(arg) ?: Job().apply { complete() }
}

@Suppress("UNCHECKED_CAST")
fun HandlerRegistry.dispatch(message: Any, output: ConnectorOutput = {}): Job? =
    (get(message::class) as? Handle<Any>)?.invoke(message, output)

typealias HandlerRegistry = Map<KClass<*>, Handle<*>>

class HandlerRegistryBuilder internal constructor() {
    internal val handlers = mutableMapOf<KClass<*>, Handle<*>>()

    operator fun HandlerRegistry.unaryPlus() {
        handlers += this
    }

    inline operator fun <reified T> Handle<T>.unaryPlus() {
        unsafePlus(T::class, this)
    }

    fun unsafePlus(type: KClass<*>, handle: Handle<*>) {
        handlers += (type to handle)
    }
}

fun createHandlers(build: HandlerRegistryBuilder.() -> Unit) =
    HandlerRegistryBuilder().apply(build).handlers

suspend fun <T> Flow<T>.collect(handle: Handle<T>, join: Boolean = false) = collect {
    handle(it).run { if (join) join() }
}
