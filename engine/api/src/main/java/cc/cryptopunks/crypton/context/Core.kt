package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.Executors

interface Core :
    Executors,
    BroadcastError.Core,
    Connection.Core,
    Repo,
    Sys {

    val serviceScope: Service.Scope

    class Module(
        private val executorsCore: Executors,
        private val broadcastErrorCore: BroadcastError.Core,
        private val connectionCore: Connection.Core,
        private val repo: Repo,
        private val sys: Sys
    ) :
        Core,
        Executors by executorsCore,
        BroadcastError.Core by broadcastErrorCore,
        Connection.Core by connectionCore,
        Repo by repo,
        Sys by sys {

        override val serviceScope = Service.Scope()
    }
}