package cc.cryptopunks.crypton.core

import cc.cryptopunks.crypton.api.Api
import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.manager.BaseManager
import cc.cryptopunks.crypton.presentation.PresentationManager
import cc.cryptopunks.crypton.repo.Repo
import cc.cryptopunks.crypton.service.Service
import cc.cryptopunks.crypton.sys.Sys
import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.ExecutorsComponent

class CoreModule(
    private val executorsComponent: ExecutorsComponent,
    private val broadcastErrorComponent: BroadcastError.Component,
    private val clientComponent: Api.Component,
    private val repoComponent: Repo.Component,
    private val repoProvider: Repo.Provider,
    private val sysComponent: Sys.Component
) :
    Core.Component,
    ExecutorsComponent by executorsComponent,
    BroadcastError.Component by broadcastErrorComponent,
    Api.Component by clientComponent,
    Repo.Component by repoComponent,
    Repo.Provider by repoProvider,
    Sys.Component by sysComponent {

    override val serviceScope = Service.Scope(broadcastError)
    override val presentationManager = PresentationManager()
    override val accountApiManager = Account.Api.Manager(apiManager as BaseManager<Account, Account.Api>)
}