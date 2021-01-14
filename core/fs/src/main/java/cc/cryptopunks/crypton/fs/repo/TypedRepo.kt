package cc.cryptopunks.crypton.fs.repo

import cc.cryptopunks.crypton.fs.Id

interface TypedRepo<T> {
    suspend operator fun plus(data: T): Id
    operator fun get(id: Id): T
    operator fun contains(id: Id): Boolean
    operator fun minus(id: Id): Boolean
//    fun free(): Long
//    fun set(): Set<Id>
    fun typeOf(any: Any): Boolean
}
