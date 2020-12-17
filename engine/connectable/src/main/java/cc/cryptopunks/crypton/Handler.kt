package cc.cryptopunks.crypton

import kotlinx.coroutines.CoroutineScope
import kotlin.reflect.KClass

val Scope.handlers: HandlerRegistry by dep()

fun <CS : CoroutineScope, S : Scoped<CS>> handle(block: Handle<CS, S>): Handle<CS, S> = block

fun createHandlers(build: HandlerRegistryBuilder.() -> Unit): HandlerRegistry =
    HandlerRegistryBuilder().apply(build).run { HandlerRegistry(handlers) }

val NonHandle: Handle<CoroutineScope, Scoped<CoroutineScope>> = { _, _ -> }

typealias Handle<S, T> = suspend S.(Output, T) -> Unit

typealias Handlers = Map<KClass<*>, Handle<Scope, Any>>
data class HandlerRegistry(val map: Handlers): Handlers by map

class HandlerRegistryBuilder internal constructor() {
    internal val handlers = mutableMapOf<KClass<*>, Handle<Scope, Any>>()

    operator fun HandlerRegistry.unaryPlus() {
        handlers += this
    }

    inline operator fun <CS : CoroutineScope, reified S : Scoped<CS>> Handle<CS, S>.unaryPlus() {
        unsafePlus(S::class, this as Handle<CoroutineScope, Scoped<CoroutineScope>>)
    }

    fun unsafePlus(type: KClass<*>, handle: Handle<CoroutineScope, Scoped<CoroutineScope>>) {
        handlers += (type to handle as Handle<Scope, Any>)
    }
}
