package cc.cryptopunks.crypton.util

import android.util.SparseArray

class SparseArrayMap<T> : SparseArray<T>(), Map<Int, T> {

    private val indexes get() = 0 until size

    override val size: Int get() = size()

    override val keys: Set<Int> get() = indexes.map { keyAt(it) }.toSet()

    override val values: Collection<T> = indexes.map { get(it)!! }

    override val entries: Set<Entry<T>>
        get() = indexes.map {
            Entry(
                key = keyAt(it),
                value = valueAt(it)
            )
        }.toSet()

    override fun containsKey(key: Int): Boolean = get(key) != null

    override fun containsValue(value: T): Boolean = (0 until size()).any { valueAt(it) == value }

    override fun get(key: Int): T? = get(key, null)

    override fun isEmpty(): Boolean = size() == 0

    data class Entry<T>(
        override val key: Int,
        override val value: T
    ) : Map.Entry<Int, T>
}