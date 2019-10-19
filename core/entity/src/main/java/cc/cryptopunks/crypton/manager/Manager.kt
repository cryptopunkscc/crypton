package cc.cryptopunks.crypton.manager

import kotlinx.coroutines.flow.Flow

typealias BaseManager<K, V> = Manager<K, V>

interface Manager<K, out V> : Flow<V> {
    suspend fun get(key: K): V
    suspend fun minus(key: K)
    operator fun contains(key: K): Boolean
    val isEmpty: Boolean
}