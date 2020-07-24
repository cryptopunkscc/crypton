package cc.cryptopunks.crypton

import kotlinx.coroutines.CoroutineScope
import kotlin.reflect.KClass

typealias Handle<S, T> = suspend S.(ConnectorOutput, T) -> Unit

fun <CS : CoroutineScope, S : Scoped<CS>> handle(block: Handle<CS, S>) = block

data class ActionError(
    val message: String?,
    val command: String
)

typealias HandlerRegistry = Map<KClass<*>, Handle<Any, Any>>

class HandlerRegistryBuilder internal constructor() {
    internal val handlers = mutableMapOf<KClass<*>, Handle<Any, Any>>()

    operator fun HandlerRegistry.unaryPlus() {
        handlers += this
    }

    inline operator fun <CS : CoroutineScope, reified S : Scoped<CS>> Handle<CS, S>.unaryPlus() {
        unsafePlus(S::class, this)
    }

    fun unsafePlus(type: KClass<*>, handle: Handle<*, *>) {
        handlers += (type to handle as Handle<Any, Any>)
    }
}

fun createHandlers(build: HandlerRegistryBuilder.() -> Unit): HandlerRegistry =
    HandlerRegistryBuilder().apply(build).handlers
