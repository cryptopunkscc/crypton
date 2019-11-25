package cc.cryptopunks.crypton.core

import cc.cryptopunks.crypton.connection.Connection
import cc.cryptopunks.crypton.repo.Repo
import cc.cryptopunks.crypton.service.Service
import cc.cryptopunks.crypton.sys.Sys
import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.ExecutorsComponent

class CoreModule(
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