package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.annotation.ApplicationScope
import cc.cryptopunks.crypton.entity.Network
import cc.cryptopunks.crypton.entity.Network.Status.*
import cc.cryptopunks.crypton.entity.Session
import cc.cryptopunks.crypton.interactor.DisconnectAccountsInteractor
import cc.cryptopunks.crypton.interactor.LoginAccountInteractor
import cc.cryptopunks.crypton.interactor.ReconnectAccountsInteractor
import cc.cryptopunks.crypton.manager.SessionManager
import cc.cryptopunks.crypton.net.Net.Event.Disconnected
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@ApplicationScope
class ReconnectAccountsService @Inject constructor(
    private val scope: Service.Scope,
    private val networkStatusFlow: Network.Sys.GetStatus,
    private val sessionManager: SessionManager,
    private val loginAccount: LoginAccountInteractor,
    private val reconnectAccount: ReconnectAccountsInteractor,
    private val disconnectDisconnect: DisconnectAccountsInteractor
) : () -> Job {

    private val log = typedLog()

    override fun invoke() = scope.launch {
        log.d("start")
        invokeOnClose { log.d("stop") }
        launch { networkStatusFlow.collect(onNetworkStatus) }
        launch { sessionManager.collect(onSessionEvent) }
    }

    private val onNetworkStatus: suspend (Network.Status) -> Unit = { status ->
        when (status) {
            is Available,
            is Changed -> reconnectAccount()
            is Unavailable -> disconnectDisconnect()
        }
    }

    private val onSessionEvent: suspend (Session.Event) -> Unit = { (session, event) ->
        when (event) {
            is Disconnected ->
                if (event.hasError) {
                    delay(2000)
                    if (!reconnectAccount.isWorking)
                        if (!session.isConnected())
                            loginAccount(session.address)
                }
        }
    }
}

