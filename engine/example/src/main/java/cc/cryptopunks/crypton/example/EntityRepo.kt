package cc.cryptopunks.crypton.example

import cc.cryptopunks.crypton.delegate.dep
import kotlinx.coroutines.CoroutineScope

val CoroutineScope.entityRepo: Entity.Repo by dep()

class EntityRepo : Entity.Repo {
    private val cache = mutableMapOf<String, Entity>()
    override suspend fun collection() = cache.values

    override suspend fun get(id: String) = cache[id]

    override suspend fun set(entity: Entity) {
        cache[entity.id] = entity
    }
}
