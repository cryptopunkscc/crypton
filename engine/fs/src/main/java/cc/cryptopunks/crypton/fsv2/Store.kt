package cc.cryptopunks.crypton.fsv2

import kotlinx.coroutines.flow.Flow

internal typealias Id = String

interface Store<T> {
    operator fun get(id: Id): T
    operator fun contains(id: Id): Boolean
    fun list(): Set<Id>
    fun additions(): Flow<Id>
    fun deletions(): Flow<Id>

    interface Write<T> {
        suspend operator fun plus(data: T): Id
        operator fun minus(id: Id): Boolean
    }

    interface ReadWrite<T> : Store<T>, Write<T>
}

typealias ByteFlowStore = Store.ReadWrite<Flow<ByteArray>>

internal typealias Type = String

interface Graph {
    fun source(id: Id): Set<Id>
    fun target(id: Id): Set<Id>
    fun type(name: Type): Set<Id>
    fun stories(): Set<Id>

    interface Writer {
        fun setRelation(target: Id, source: Id)
        fun setType(id: Id, type: String)
        fun remove(id: Id)
    }

    interface ReadWrite : Graph, Writer
}
