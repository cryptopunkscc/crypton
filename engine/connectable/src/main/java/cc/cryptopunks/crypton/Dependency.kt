package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.elements
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

data class Dependency<T>(
    val instance: T,
    override val key: Key<T>,
) : CoroutineContext.Element {
    data class Key<T>(val any: Any) : CoroutineContext.Key<Dependency<T>>
}

inline fun <reified T> T.asDep(any: Any = T::class.java) = Dependency(this, depKey(any))

inline fun <reified T> depKey(any: Any = T::class.java): Dependency.Key<T> = Dependency.Key(any)

inline fun <reified T> dep(tag: Any = T::class.java) = DynamicDependency(depKey<T>(tag))

inline fun <reified T> CoroutineScope.dep(key: Dependency.Key<T> = depKey()) =
    lazy { get(key)!! }

inline fun <reified T> CoroutineScope.get(key: Dependency.Key<T> = depKey()): T? =
    coroutineContext[key]?.instance

fun cryptonContext(
    vararg any: Any,
): CoroutineContext = any
    .map {
        if (it is CoroutineContext) it
        else Dependency(it, Dependency.Key(it::class.java))
    }
    .reduce(CoroutineContext::plus)

class DynamicDependency<T>(
    private val key: Dependency.Key<T>,
) : ReadOnlyProperty<CoroutineScope, T> {
    override fun getValue(thisRef: CoroutineScope, property: KProperty<*>): T =
        requireNotNull(thisRef.coroutineContext[key]) {
            """
Cannot find dependency: ${property.name} with key: $key
available dependencies:
${thisRef.coroutineContext.elements().joinToString("\n")}
            """.trimMargin()
        }.instance
}
