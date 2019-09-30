package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.domain.interactor.*
import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Account.Status.Connected
import cc.cryptopunks.crypton.entity.Account.Status.Connecting
import javax.inject.Inject

class AccountItemViewModel @Inject constructor(
    private val connectAccount: ConnectAccountInteractor,
    private val disconnectAccount: DisconnectAccountInteractor,
    private val deleteAccount: DeleteAccountInteractor,
    private val unregisterAccount: UnregisterAccountInteractor
) {
    var account = Account.Empty

    val name get() = account.address

    val status get() = account.status.name

    val isChecked get() = account.run { status == Connected || status == Connecting }

    val isConnected get() = account.status == Connected

    fun toggleConnection() =
        when (account.status) {
            Connected -> disconnectAccount

            Connecting -> when (isChecked) {
                true -> disconnectAccount
                else -> connectAccount
            }
            else -> connectAccount
        }(account)

    fun remove(deleteFromServer: Boolean) =
        when (deleteFromServer) {
            true -> unregisterAccount
            false -> deleteAccount
        }(account)
}