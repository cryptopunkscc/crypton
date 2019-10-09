package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.feature.account.interactor.*

internal interface IntegrationTestComponent {

    val accountRepo: Account.Repo
    val clientCache: Client.Cache

    val addAccount: AddAccountInteractor
    val connectAccount: ConnectAccountInteractor
    val registerAccount: RegisterAccountInteractor
    val disconnectAccount: DisconnectAccountInteractor
    val deleteAccount: DeleteAccountInteractor
    val unregisterAccount: UnregisterAccountInteractor
    val reconnectAccounts: ReconnectAccountsInteractor

    fun clearDatabase()
}