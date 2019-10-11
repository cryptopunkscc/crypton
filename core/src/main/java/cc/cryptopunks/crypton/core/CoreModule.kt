package cc.cryptopunks.crypton.core

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.repo.Repo
import cc.cryptopunks.crypton.service.Service
import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.ExecutorsComponent

class CoreModule(
    private val executorsComponent: ExecutorsComponent,
    private val repoComponent: Repo.Component,
    private val clientComponent: Client.Component
) :
    Core.Component,
    ExecutorsComponent by executorsComponent,
    Repo.Component by repoComponent,
    Client.Component by clientComponent,
    BroadcastError.Component by BroadcastError.Module() {

    override val serviceScope = Service.Scope(broadcastError)
}