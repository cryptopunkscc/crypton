package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.interactor.*
import cc.cryptopunks.crypton.manager.SessionManager
import cc.cryptopunks.crypton.service.ReconnectAccountsService

internal interface IntegrationTestComponent {

    val accountRepo: Account.Repo
    val clientManager: SessionManager

    val addAccount: AddAccountInteractor
    val loginAccount: LoginAccountInteractor
    val registerAccount: RegisterAccountInteractor
    val disconnectAccount: DisconnectAccountInteractor
    val deleteAccount: DeleteAccountInteractor
    val unregisterAccount: UnregisterAccountInteractor
    val reconnectAccounts: ReconnectAccountsService

    fun clearDatabase()
}