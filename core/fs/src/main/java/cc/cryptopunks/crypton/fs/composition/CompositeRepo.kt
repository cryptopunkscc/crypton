package cc.cryptopunks.crypton.fs.composition

import cc.cryptopunks.crypton.fs.Data
import cc.cryptopunks.crypton.fs.Id
import cc.cryptopunks.crypton.fs.repo.TypedRepo

class CompositeRepo<T : Data>(
    private val repo: List<TypedRepo<out T>>
) : TypedRepo<T> {
    constructor(vararg args: TypedRepo<out T>) : this(args.toList())

    override fun get(id: Id): T = repo.first { id in it }[id]
    override fun contains(id: Id): Boolean = repo.any { id in it }
    override fun minus(id: Id): Boolean = repo.any { it - id }
    override fun typeOf(any: Any): Boolean = repo.any { it.typeOf(any) }
    override suspend fun plus(data: T): Id = repo
        .first { it.typeOf(data) }
        .let { it as TypedRepo<Data> } + data
}
