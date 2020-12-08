package cc.cryptopunks.crypton

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

data class Dependency<T>(
    val instance: T,
    override val key: CoroutineContext.Key<Dependency<T>>,
) : CoroutineContext.Element {
    data class Key<T>(val any: Any) : CoroutineContext.Key<Dependency<T>>
}

inline fun <reified T> T.dep() = Dependency(this, depKey())

inline fun <reified T> depKey(): Dependency.Key<T> = Dependency.Key(T::class.java)

inline fun <reified T> dep() = DependencyDelegate<T>(depKey())

class DependencyDelegate<T>(
    private val key: Dependency.Key<T>,
) : ReadOnlyProperty<CoroutineScope, T> {
    override fun getValue(thisRef: CoroutineScope, property: KProperty<*>): T =
        requireNotNull(thisRef.coroutineContext[key]).instance
}
