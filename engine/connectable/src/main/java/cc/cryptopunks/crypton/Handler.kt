package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.TypedLog
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.reflect.KClass


fun <T : Any> CoroutineScope.handle(
    handle: suspend T.(ConnectorOutput) -> Unit
): Handle<T> = Handler(this, handle)

interface Handle<T> {
    operator fun invoke(arg: T, output: ConnectorOutput = {}): Job

    data class Error(
        val message: String?,
        val command: String
    )
}

private class Handler<T>(
    scope: CoroutineScope,
    val handle: suspend T.(ConnectorOutput) -> Unit
) : Handle<T>,
    CoroutineScope by scope {

    override fun invoke(arg: T, output: ConnectorOutput) = launch {
        try {
            arg.handle(output)
        } catch (e: Throwable) {
            when (e) {
                is CancellationException -> coroutineContext[TypedLog]?.d("Handle cancelled by: ${e.message} $arg")
                else -> Handle.Error(e.message ?: e.javaClass.name, arg.toString()).also {
                    e.printStackTrace()
                    output(it)
                }
            }
        }
    }
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

suspend fun <T> Flow<T>.collect(
    handle: Handle<T>,
    output: ConnectorOutput = {},
    join: Boolean = false
) = collect {
    handle(it, output).run { if (join) join() }
}
