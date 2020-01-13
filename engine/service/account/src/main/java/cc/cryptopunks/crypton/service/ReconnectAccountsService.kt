package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Net.Disconnected
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
import javax.inject.Singleton

@Singleton
class ReconnectAccountsService @Inject constructor(
    private val scope: Service.Scope,
    private val getNetworkStatus: Network.Sys.GetStatus,
    private val sessionManager: SessionManager,
    private val reconnect: ReconnectAccountsInteractor,
    private val disconnect: DisconnectAccountsInteractor
) : () -> Job {

    private val log = typedLog()

    override fun invoke() = scope.launch {
        log.d("start")
        invokeOnClose { log.d("stop") }
        launch { getNetworkStatus.collect(handleNetworkStatus) }
        launch { sessionManager.collect(handleSessionEvent) }
    }

    private val handleNetworkStatus: suspend (Network.Status) -> Unit = { status ->
        when (status) {
            is Available,
            is Changed -> reconnect()
            is Unavailable -> disconnect()
        }
    }

    private val handleSessionEvent: suspend (Session.Event) -> Unit = { (session, event) ->
        if (event is Disconnected)
            if (event.hasError) {
                waitForNetworkConnection()
                if (!session.isConnected())
                    if (getNetworkStatus().isConnected)
                        reconnect(session.address) else
                        disconnect(session.address)
            }
    }

    private suspend fun waitForNetworkConnection() = delay(2000)

}

