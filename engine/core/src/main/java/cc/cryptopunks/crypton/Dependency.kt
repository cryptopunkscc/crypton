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
