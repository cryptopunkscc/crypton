package cc.cryptopunks.crypton.account.presentation.viewmodel

import cc.cryptopunks.crypton.account.domain.command.ConnectAccount
import cc.cryptopunks.crypton.account.domain.command.DeleteAccount
import cc.cryptopunks.crypton.account.domain.command.DisconnectAccount
import cc.cryptopunks.crypton.account.domain.command.RemoveAccount
import cc.cryptopunks.crypton.core.entity.Account
import cc.cryptopunks.crypton.core.entity.Account.Status.Connected
import cc.cryptopunks.crypton.core.entity.Account.Status.Connecting
import cc.cryptopunks.crypton.core.util.AsyncExecutor
import javax.inject.Inject

class AccountItemViewModel @Inject constructor(
    private val async: AsyncExecutor,
    private val connectAccount: ConnectAccount,
    private val disconnectAccount: DisconnectAccount,
    private val removeAccount: RemoveAccount,
    private val deleteAccount: DeleteAccount
) {
    var account = Account.Empty

    val name get() = account.jid

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