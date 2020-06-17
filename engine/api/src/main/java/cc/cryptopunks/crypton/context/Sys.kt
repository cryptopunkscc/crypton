package cc.cryptopunks.crypton.context

interface Sys {

    val routeSys: Route.Sys
    val indicatorSys: Indicator.Sys
    val notificationSys: Notification.Sys
    val clipboardSys: Clip.Board.Sys
    val networkSys: Network.Sys

    val createRouteSys: () -> Route.Sys
}
