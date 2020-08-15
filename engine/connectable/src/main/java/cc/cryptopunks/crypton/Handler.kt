package cc.cryptopunks.crypton

import kotlinx.coroutines.CoroutineScope
import kotlin.reflect.KClass

fun <CS : CoroutineScope, S : Scoped<CS>> handle(block: Handle<CS, S>) = block

fun createHandlers(build: HandlerRegistryBuilder.() -> Unit): HandlerRegistry =
    HandlerRegistryBuilder().apply(build).handlers


typealias Handle<S, T> = suspend S.(Output, T) -> Unit

typealias HandlerRegistry = Map<KClass<*>, Handle<Scope, Any>>

class HandlerRegistryBuilder internal constructor() {
    internal val handlers = mutableMapOf<KClass<*>, Handle<Scope, Any>>()

    operator fun HandlerRegistry.unaryPlus() {
        handlers += this
    }

    inline operator fun <CS : CoroutineScope, reified S : Scoped<CS>> Handle<CS, S>.unaryPlus() {
        unsafePlus(S::class, this)
    }

    fun unsafePlus(type: KClass<*>, handle: Handle<*, *>) {
        handlers += (type to handle as Handle<Scope, Any>)
    }
}
