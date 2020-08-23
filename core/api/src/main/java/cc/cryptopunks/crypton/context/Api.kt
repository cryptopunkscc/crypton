package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.Scoped
import cc.cryptopunks.crypton.inContext

object Api {
    interface Event
}

fun Scoped<*>.inContext(vararg addresses: Address) = inContext(addresses.map { it.id })
