package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.domain.interactor.*
import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Account.Status.Connected
import cc.cryptopunks.crypton.entity.Account.Status.Connecting
import javax.inject.Inject

class AccountItemViewModel @Inject constructor(
    private val connectAccount: ConnectAccountInteractor,
    private val disconnectAccount: DisconnectAccountInteractor,
    private val removeAccount: RemoveAccountInteractor,
    private val deleteAccount: DeleteAccountInteractor
) {
    var account = Account.Empty

    val name get() = account.remoteId

    val status get() = account.status.name

    val isChecked get() = account.run { status == Connected || status == Connecting }

    val isConnected get() = account.status == Connected

    fun toggleConnection() = with(account) {
        when (status) {
            Connected -> disconnectAccount

            Connecting -> when (isChecked) {
                true -> disconnectAccount
                else -> connectAccount
            }

            else -> connectAccount
        }(id)
    }

    fun remove(deleteFromServer: Boolean) = with(account) {
        when (deleteFromServer) {
            true -> deleteAccount
            false -> removeAccount
        }(id)
    }
}