package cc.cryptopunks.crypton.factory

import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.Handle
import cc.cryptopunks.crypton.Handler

inline fun <reified A: Action> handler(
    noinline handle: Handle<A>,
) = Handler(
    key = Handler.Key(A::class.java),
    handle = handle
)
