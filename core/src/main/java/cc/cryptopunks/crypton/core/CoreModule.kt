package cc.cryptopunks.crypton.core

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.presentation.PresentationManager
import cc.cryptopunks.crypton.repo.Repo
import cc.cryptopunks.crypton.service.Service
import cc.cryptopunks.crypton.sys.Sys
import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.ExecutorsComponent

class CoreModule(
    private val executorsComponent: ExecutorsComponent,
    private val broadcastErrorComponent: BroadcastError.Component,
    private val clientComponent: Client.Component,
    private val repoComponent: Repo.Component,
    private val sysComponent: Sys.Component
) :
    Core.Component,
    ExecutorsComponent by executorsComponent,
    BroadcastError.Component by broadcastErrorComponent,
    Client.Component by clientComponent,
    Repo.Component by repoComponent,
    Sys.Component by sysComponent {

    override val serviceScope = Service.Scope(broadcastError)
    override val presentationManager = PresentationManager()
}