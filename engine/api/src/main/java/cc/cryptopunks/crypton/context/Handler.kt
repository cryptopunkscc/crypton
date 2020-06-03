package cc.cryptopunks.crypton.context

import kotlinx.coroutines.Job
import kotlin.reflect.KClass


fun <T : Any> handle(handle: T.(ConnectorOutput) -> Job): Handle<T> = Handler(handle)

interface Handle<T> {
    operator fun T.invoke(output: ConnectorOutput = {}): Job
}

private class Handler<T>(val handle: T.(ConnectorOutput) -> Job) : Handle<T> {
    override fun T.invoke(output: ConnectorOutput) = handle(output)
}

@Suppress("UNCHECKED_CAST")
fun HandlerRegistry.dispatch(message: Any, output: ConnectorOutput = {}): Job? =
    (get(message::class) as? Handle<Any>)?.run { message(output) }

typealias HandlerRegistry = Map<KClass<*>, Handle<*>>

typealias HandlerRegistryBuilder = MutableMap<KClass<*>, Handle<*>>

fun handlerRegistry(build: HandlerRegistryBuilder.() -> Unit): HandlerRegistry =
    mutableMapOf<KClass<*>, Handle<*>>().apply(build)

inline operator fun <reified T> HandlerRegistryBuilder.plus(handle: Handle<T>) =
    plusAssign(T::class to handle)
