package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.create.inScope

object Api {
    interface Event
}

fun Action.inScope(vararg addresses: Address) = inScope(addresses.map { it.id })
