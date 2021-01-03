package cc.cryptopunks.crypton.util

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

typealias Instance = CoroutineContextObject
typealias Singleton = CoroutineContextSingleton

interface CoroutineContextObject :
    CoroutineContext.Element,
    CoroutineContext.Key<CoroutineContextObject> {
    override val key get() = this
}

interface CoroutineContextSingleton :
    CoroutineContext.Element,
    CoroutineContext.Key<CoroutineContextObject> {
    override val key get() = Key(javaClass)
    data class Key<T: CoroutineContext.Element>(val type: Class<T>) : CoroutineContext.Key<T>
}

class DynamicSingleton<T: CoroutineContext.Element>(
    private val key: CoroutineContextSingleton.Key<T>,
) : ReadOnlyProperty<CoroutineScope, T> {
    override fun getValue(thisRef: CoroutineScope, property: KProperty<*>): T =
        requireNotNull(thisRef.coroutineContext[key]) {
            """
Cannot find singleton: ${property.name} with key: $key
available singletons:
${thisRef.coroutineContext.mapNotNull { it as? Singleton }.joinToString("\n")}
            """.trimMargin()
        }
}


inline fun <reified T: CoroutineContext.Element> single() =
    DynamicSingleton(CoroutineContextSingleton.Key(T::class.java))


operator fun CoroutineScope.contains(key: CoroutineContext.Key<*>): Boolean =
    coroutineContext[key] != null

fun CoroutineContext.elements(
    predicate: (CoroutineContext.Element) -> Boolean = { true },
): List<CoroutineContext.Element> =
    mutableListOf<CoroutineContext.Element>().also {
        fold(it) { acc, element ->
            if (predicate(element)) acc += element
            acc
        }
    }

fun <T> CoroutineContext.mapNotNull(
    f: (CoroutineContext.Element) -> T?,
): List<T> =
    mutableListOf<T>().also { list ->
        fold(list) { acc, element ->
            f(element)?.let { acc += it }
            acc
        }
    }
