package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.sys.*

class JvmSys : Sys {
    override val routeSys: Route.Sys by lazy { RouteSys() }
    override val indicatorSys: Indicator.Sys by lazy { IndicatorSys() }
    override val notificationSys: Notification.Sys by lazy { NotificationSys() }
    override val clipboardSys: Clip.Board.Sys by lazy { ClipBoardSys() }
    override val networkSys: Network.Sys by lazy { NetworkSys() }
    override fun createRouteSys(): Route.Sys = RouteSys()
}
