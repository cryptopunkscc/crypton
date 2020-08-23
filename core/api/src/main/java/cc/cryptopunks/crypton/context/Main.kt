package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.Scoped

object Main {
    interface Action : Scoped<RootScope>
}
