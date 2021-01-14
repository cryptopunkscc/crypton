package cc.cryptopunks.crypton.get

import cc.cryptopunks.crypton.Scoped

fun Scoped.action(): Any = when (next) {
    is Scoped -> next.action()
    else -> next
}
