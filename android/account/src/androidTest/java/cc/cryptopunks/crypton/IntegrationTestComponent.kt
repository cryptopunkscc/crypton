package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.interactor.*
import cc.cryptopunks.crypton.service.ReconnectAccountsService
import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.entity.Account

internal interface IntegrationTestComponent {

    val accountRepo: Account.Repo
    val clientManager: Client.Manager

    val addAccount: AddAccountInteractor
    val connectAccount: ConnectAccountInteractor
    val registerAccount: RegisterAccountInteractor
    val disconnectAccount: DisconnectAccountInteractor
    val deleteAccount: DeleteAccountInteractor
    val unregisterAccount: UnregisterAccountInteractor
    val reconnectAccounts: ReconnectAccountsService

    fun clearDatabase()
}