package cc.cryptopunks.crypton.example

import cc.cryptopunks.crypton.Handle
import cc.cryptopunks.crypton.Scoped
import cc.cryptopunks.crypton.createHandlers
import kotlinx.coroutines.CoroutineScope

internal fun getAllHandlers() = createHandlers {
    +setEntity
    +getAllEntities
    +getDetails
}

private val setEntity =
    handle { _, arg: Entity.Command.Set ->
        entityRepo.set(arg.entity)
    }

private val getAllEntities =
    handle { out, _: Entity.Query.All ->
        out(Entity.Many(entityRepo.collection()))
    }

private val getDetails =
    handle { out, _: Details.Query.Get ->
        out(Details("Details $id"))
    }

fun <CS: CoroutineScope, S: Scoped<CS>> handle(block: Handle<CS, S>) = block
