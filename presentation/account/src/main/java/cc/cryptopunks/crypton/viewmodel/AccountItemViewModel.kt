package cc.cryptopunks.crypton.viewmodel

import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.interactor.DeleteAccountInteractor
import cc.cryptopunks.crypton.interactor.DisconnectAccountInteractor
import cc.cryptopunks.crypton.interactor.LoginAccountInteractor
import cc.cryptopunks.crypton.interactor.UnregisterAccountInteractor
import kotlinx.coroutines.Job
import javax.inject.Inject

// TODO this class needs some work due to api changes
class AccountItemViewModel @Inject constructor(
    private val loginAccount: LoginAccountInteractor,
    private val disconnectAccount: DisconnectAccountInteractor,
    private val deleteAccount: DeleteAccountInteractor,
    private val unregisterAccount: UnregisterAccountInteractor
) {
    var account = Address.Empty

    val name get() = account

    val status: String get() = TODO() //account.status.name

    val isChecked: Boolean get() = TODO() //account.run { status == Connected || status == Connecting }

    val isConnected: Boolean get() = TODO() //account.status == Connected

    fun toggleConnection(): Job = TODO()
//        when (account.status) {
//            Connected -> disconnectAccount
//
//            Connecting -> when (isChecked) {
//                true -> disconnectAccount
//                else -> loginAccount
//            }
//            else -> loginAccount
//        }(account)

    fun remove(deleteFromServer: Boolean) =
        when (deleteFromServer) {
            true -> unregisterAccount
            false -> deleteAccount
        }(account)
}