package cc.cryptopunks.crypton.activity

import android.os.Bundle
import androidx.core.os.bundleOf

fun Bundle?.toMap() = BundleMap(this ?: Bundle())

class BundleMap(val bundle: Bundle = Bundle()) : MutableMap<String, Any?> {

    override val size: Int
        get() = bundle.size()

    override val entries: MutableSet<MutableMap.MutableEntry<String, Any?>>
        get() = keys.map { Entry(it, get(it)) }.toMutableSet()

    override val keys: MutableSet<String>
        get() = bundle.keySet()

    override val values
        get() = keys.map { get(it) }.toMutableList()

    override fun containsKey(key: String) = bundle.containsKey(key)

    override fun containsValue(value: Any?) = values.contains(value)

    override fun get(key: String): Any? = bundle.get(key)

    override fun isEmpty() = bundle.isEmpty

    override fun clear() = bundle.clear()

    override fun put(key: String, value: Any?): Any? = get(key).also {
        bundle.putAll(bundleOf(key to value))
    }

    override fun putAll(from: Map<out String, Any?>) {
        val args = from.map { (key, value) -> key to value }.toTypedArray()
        bundle.putAll(bundleOf(*args))
    }

    override fun remove(key: String): Any? = bundle.get(key).also { bundle.remove(key) }

    private class Entry(
        override val key: String,
        override var value: Any?
    ) : MutableMap.MutableEntry<String, Any?> {
        override fun setValue(newValue: Any?): Any? = value?.also {
            value = it
        }
    }
}