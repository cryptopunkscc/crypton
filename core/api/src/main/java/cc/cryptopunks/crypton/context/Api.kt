package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.Scoped
import cc.cryptopunks.crypton.context

object Api {
    interface Event
}

fun Scoped<*>.context(vararg addresses: Address) = context(addresses.map { it.id })
