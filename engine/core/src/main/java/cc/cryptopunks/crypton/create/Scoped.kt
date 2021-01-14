package cc.cryptopunks.crypton.create

import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.Scoped

fun Action.inScope(vararg ids: String): Action =
    inScope(ids.toList())

fun Action.inScope(ids: List<String>): Action =
    ids.foldRight(this) { id, acc ->
        Scoped(
            id = id,
            next = acc
        )
    }

