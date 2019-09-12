package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.domain.interactor.ConnectAccount
import cc.cryptopunks.crypton.domain.interactor.DeleteAccount
import cc.cryptopunks.crypton.domain.interactor.DisconnectAccount
import cc.cryptopunks.crypton.domain.interactor.RemoveAccount
import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Account.Status.Connected
import cc.cryptopunks.crypton.entity.Account.Status.Connecting
import cc.cryptopunks.crypton.util.AsyncExecutor
import javax.inject.Inject

class AccountItemViewModel @Inject constructor(
    private val async: AsyncExecutor,
    private val connectAccount: ConnectAccount,
    private val disconnectAccount: DisconnectAccount,
    private val removeAccount: RemoveAccount,
    private val deleteAccount: DeleteAccount
) {
    var account = Account.Empty

    val name get() = account.remoteId

    val status get() = account.status.name

    val isChecked get() = account.run { status == Connected || status == Connecting }

    val isConnected get() = account.status == Connected

    fun toggleConnection() = with(account) {
        async(
            task = when (status) {
                Connected -> disconnectAccount

                Connecting -> when (isChecked) {
                    true -> disconnectAccount
                    else -> connectAccount
                }

                else -> connectAccount
            }
        )(id)
    }

    fun remove(deleteFromServer: Boolean) = with(account) {
        async(
            task = when (deleteFromServer) {
                true -> deleteAccount
                false -> removeAccount
            }
        )(id)
    }
}