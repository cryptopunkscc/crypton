package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.manager.SessionServiceManager
import cc.cryptopunks.crypton.util.log
import javax.inject.Inject

class AppService @Inject constructor(
    private val toggleIndicatorService: ToggleIndicatorService,
    private val reconnectAccountsService: ReconnectAccountsService,
    private val sessionServiceManager: SessionServiceManager

) : () -> Unit by {
    log<AppService>("start")
    toggleIndicatorService()
    reconnectAccountsService()
    sessionServiceManager()
    log<AppService>("stop")
} {

    interface Component {
        val appService: AppService
    }
}