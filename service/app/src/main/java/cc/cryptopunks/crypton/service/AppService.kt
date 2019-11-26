package cc.cryptopunks.crypton.service

interface AppServices {
    val errorService: ErrorService
    val toggleIndicatorService: ToggleIndicatorService
    val reconnectAccountsService: ReconnectAccountsService
    val sessionService: SessionService
}

fun AppServices.appServices() {
    errorService()
    toggleIndicatorService()
    reconnectAccountsService()
    sessionService()
}