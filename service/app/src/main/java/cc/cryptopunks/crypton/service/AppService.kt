package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.util.log
import javax.inject.Inject

class AppService @Inject constructor(
    private val errorService: ErrorService,
    private val toggleIndicatorService: ToggleIndicatorService,
    private val reconnectAccountsService: ReconnectAccountsService,
    private val sessionService: SessionService

) : () -> Unit by {
    log<AppService>("start")
    errorService()
    toggleIndicatorService()
    reconnectAccountsService()
    sessionService()
    log<AppService>("stop")
} {

    interface Component {
        val appService: AppService
    }
}