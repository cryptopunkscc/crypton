package cc.cryptopunks.crypton.example

class EntityRepo : Entity.Repo {
    private val cache = mutableMapOf<String, Entity>()
    override suspend fun collection() = cache.values

    override suspend fun get(id: String) = cache[id]

    override suspend fun set(entity: Entity) {
        cache[entity.id] = entity
    }
}
