package cc.cryptopunks.crypton.example

import cc.cryptopunks.crypton.Scoped

data class Entity(
    val id: String
) {

    data class Many(val entities: Collection<Entity>)

    object Command {
        data class Set(val entity: Entity) : Scoped<RootScope>
    }

    object Query {
        object All : Scoped<RootScope>
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
        object Get :
            Scoped<NestedScope>
    }
}
