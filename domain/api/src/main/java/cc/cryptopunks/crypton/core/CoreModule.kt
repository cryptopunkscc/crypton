package cc.cryptopunks.crypton.core

import cc.cryptopunks.crypton.net.Net
import cc.cryptopunks.crypton.repo.Repo
import cc.cryptopunks.crypton.service.Service
import cc.cryptopunks.crypton.sys.Sys
import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.ExecutorsComponent

class CoreModule(
    private val executorsComponent: ExecutorsComponent,
    private val broadcastErrorComponent: BroadcastError.Component,
    private val netComponent: Net.Component,
    private val repo: Repo,
    private val sys: Sys
) :
    Core,
    ExecutorsComponent by executorsComponent,
    BroadcastError.Component by broadcastErrorComponent,
    Net.Component by netComponent,
    Repo by repo,
    Sys by sys {

    override val serviceScope = Service.Scope()
}