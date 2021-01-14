package cc.cryptopunks.crypton.example

import cc.cryptopunks.crypton.Action

data class Entity(
    val id: String
) {

    data class Many(val entities: Collection<Entity>)

    object Command {
        data class Set(val entity: Entity) : Action
    }

    object Query {
        object All : Action
    }

    interface Repo {
        suspend fun collection(): Collection<Entity>
        suspend fun get(id: String) : Entity?
        suspend fun set(entity: Entity)
    }
}

data class Details(
    val string: String
) {

    object Query {
        object Get : Action
    }
}
