package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.interactor.DeleteAccountInteractor
import cc.cryptopunks.crypton.interactor.UnregisterAccountInteractor
import cc.cryptopunks.crypton.selector.AccountListSelector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountListService @Inject constructor(
    private val getAccounts: AccountListSelector,
    private val navigationService: OptionItemNavigationService,
//    private val loginAccount: LoginAccountInteractor, TODO()
//    private val disconnectAccount: DisconnectAccountInteractor,
    private val deleteAccount: DeleteAccountInteractor,
    private val unregisterAccount: UnregisterAccountInteractor
) : Connectable {

    data class ToggleConnection(val account: Address)
    data class Remove(val account: Address, val deleteFromServer: Boolean)
    data class Accounts(val list: List<Address>)

    override val coroutineContext =
        SupervisorJob() + Dispatchers.IO

    override fun Connector.connect(): Job = launch {
        launch { navigationService() }
        launch { getAccounts().map { Accounts(it) }.collect(output) }
        launch {
            input.collect { arg ->
                when (arg) {
                    is ToggleConnection -> arg()
                    is Remove -> arg()
                }
            }
        }
    }

    private operator fun ToggleConnection.invoke(): Unit = TODO()

    private operator fun Remove.invoke() = when (deleteFromServer) {
        true -> unregisterAccount
        false -> deleteAccount
    }(account)

    interface Core {
        val accountListService: AccountListService
    }
}