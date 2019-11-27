package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.annotation.ApplicationScope
import cc.cryptopunks.crypton.context.Net.Event.Disconnected
import cc.cryptopunks.crypton.context.Network
import cc.cryptopunks.crypton.context.Network.Status.*
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.interactor.DisconnectAccountsInteractor
import cc.cryptopunks.crypton.interactor.ReconnectAccountsInteractor
import cc.cryptopunks.crypton.manager.SessionManager
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
    private val reconnect: ReconnectAccountsInteractor,
    private val disconnect: DisconnectAccountsInteractor
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
            is Changed -> reconnect()
            is Unavailable -> disconnect()
        }
    }

    private val onSessionEvent: suspend (Session.Event) -> Unit = { (session, event) ->
        when (event) {
            is Disconnected ->
                if (event.hasError) {
                    delay(2000)
                    if (!session.isConnected())
                        reconnect(session.address)
                }
        }
    }
}

