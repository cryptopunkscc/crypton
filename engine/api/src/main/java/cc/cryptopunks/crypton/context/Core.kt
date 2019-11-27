package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.ExecutorsComponent

interface Core :
    ExecutorsComponent,
    BroadcastError.Component,
    Connection.Component,
    Repo,
    Sys {

    val serviceScope: Service.Scope

    class Module(
        private val executorsComponent: ExecutorsComponent,
        private val broadcastErrorComponent: BroadcastError.Component,
        private val connectionComponent: Connection.Component,
        private val repo: Repo,
        private val sys: Sys
    ) :
        Core,
        ExecutorsComponent by executorsComponent,
        BroadcastError.Component by broadcastErrorComponent,
        Connection.Component by connectionComponent,
        Repo by repo,
        Sys by sys {

        override val serviceScope = Service.Scope()
    }
}