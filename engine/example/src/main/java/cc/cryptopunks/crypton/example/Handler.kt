package cc.cryptopunks.crypton.example

import cc.cryptopunks.crypton.create.cryptonContext
import cc.cryptopunks.crypton.create.handler

internal fun getAllHandlers() = cryptonContext(
    setEntity,
    getAllEntities,
    getDetails,
)

private val setEntity =
    handler { _, arg: Entity.Command.Set ->
        entityRepo.set(arg.entity)
    }

private val getAllEntities =
    handler { out, _: Entity.Query.All ->
        out(Entity.Many(entityRepo.collection()))
    }

private val getDetails =
    handler { out, _: Details.Query.Get ->
        out(Details("Details ${javaClass.name}"))
    }
