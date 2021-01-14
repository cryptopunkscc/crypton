package cc.cryptopunks.crypton.fsv2

import kotlinx.coroutines.flow.Flow

internal typealias Id = String

interface Store<T> {
    suspend operator fun plus(data: T): Id
    operator fun get(id: Id): T
    operator fun minus(id: Id): Boolean
    operator fun contains(id: Id): Boolean
    fun set(): Set<Id>
    fun additions(): Flow<Id>
}

internal typealias Type = String

interface Graph {
    fun source(id: Id): Set<Id>
    fun target(id: Id): Set<Id>
    fun type(name: Type): Set<Id>
}
