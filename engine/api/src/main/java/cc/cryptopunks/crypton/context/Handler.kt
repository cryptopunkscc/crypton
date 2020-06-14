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

typealias HandlerRegistryBuilder = MutableMap<KClass<*>, Handle<*>>

fun createHandlers(build: HandlerRegistryBuilder.() -> Unit): HandlerRegistry =
    mutableMapOf<KClass<*>, Handle<*>>().apply(build)

inline operator fun <reified T> HandlerRegistryBuilder.plus(handle: Handle<T>) =
    plusAssign(T::class to handle)

suspend fun <T> Flow<T>.collect(handle: Handle<T>) =
    collect { handle(it) }
