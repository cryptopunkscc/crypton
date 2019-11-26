package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.annotation.ApplicationScope
import cc.cryptopunks.crypton.entity.Network
import cc.cryptopunks.crypton.entity.Network.Status.*
import cc.cryptopunks.crypton.interactor.DisconnectAccountsInteractor
import cc.cryptopunks.crypton.interactor.ReconnectAccountsInteractor
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@ApplicationScope
class ReconnectAccountsService @Inject constructor(
    private val scope: Service.Scope,
    private val networkStatusFlow: Network.Sys.GetStatus,
    private val reconnect: ReconnectAccountsInteractor,
    private val disconnect: DisconnectAccountsInteractor
) : () -> Job {

    private val log = typedLog()

    override fun invoke() = scope.launch {
        log.d("start")
        invokeOnClose { log.d("stop") }
        networkStatusFlow.collect { status ->
            when (status) {
                is Available,
                is Changed -> reconnect()
                is Unavailable -> disconnect()
            }
        }
    }
}

