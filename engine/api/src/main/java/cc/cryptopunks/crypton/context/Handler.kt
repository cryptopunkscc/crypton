package cc.cryptopunks.crypton.context

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlin.reflect.KClass


fun <T : Any> handle(handle: T.(ConnectorOutput) -> Job): Handle<T> = Handler(handle)

interface Handle<T> {
    operator fun invoke(arg: T, output: ConnectorOutput = {}): Job
}

private class Handler<T>(val handle: T.(ConnectorOutput) -> Job) : Handle<T> {
    override fun invoke(arg: T, output: ConnectorOutput) = arg.handle(output)
}

@Suppress("UNCHECKED_CAST")
fun HandlerRegistry.dispatch(message: Any, output: ConnectorOutput = {}): Job? =
    (get(message::class) as? Handle<Any>)?.invoke(message, output)

typealias HandlerRegistry = Map<KClass<*>, Handle<*>>

class HandlerRegistryBuilder internal constructor() {
    internal val handlers = mutableMapOf<KClass<*>, Handle<*>>()

    fun unsafePlus(type: KClass<*>, handle: Handle<*>) {
        handlers += (type to handle)
    }

    inline operator fun <reified T> Handle<T>.unaryPlus() {
        unsafePlus(T::class, this)
    }
}

fun createHandlers(build: HandlerRegistryBuilder.() -> Unit): HandlerRegistry =
    HandlerRegistryBuilder().apply(build).handlers

inline operator fun <reified T> HandlerRegistryBuilder.plus(handle: Handle<T>) =
    unsafePlus(T::class, handle)

suspend fun <T> Flow<T>.collect(handle: Handle<T>) =
    collect { handle(it) }
