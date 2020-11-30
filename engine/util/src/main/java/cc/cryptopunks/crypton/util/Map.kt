package cc.cryptopunks.crypton.util

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <R : MutableMap<String, Any?>, T> mutableMapProperty(
    name: String? = null,
    defaultValue: () -> T
) = object : ReadWriteProperty<R, T> {

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: R, property: KProperty<*>): T =
        thisRef.getOrElse(name ?: property.name, defaultValue) as T

    override fun setValue(thisRef: R, property: KProperty<*>, value: T) =
        thisRef.set(name ?: property.name, value as? Any)
}
