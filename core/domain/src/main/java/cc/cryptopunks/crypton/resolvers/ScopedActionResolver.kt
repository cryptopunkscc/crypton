package cc.cryptopunks.crypton.resolvers

import cc.cryptopunks.crypton.Resolve
import cc.cryptopunks.crypton.Scoped

fun scopedActionResolver(): Resolve = { action ->
    if (action !is Scoped<*>) null
    else Scoped.Resolved(this, action)
}
