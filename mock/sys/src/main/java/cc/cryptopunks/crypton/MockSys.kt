package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.mock.ClipBoardSys
import cc.cryptopunks.crypton.mock.IndicatorSys
import cc.cryptopunks.crypton.mock.NetworkSys
import cc.cryptopunks.crypton.mock.NotificationSys
import cc.cryptopunks.crypton.mock.RouteSys

class MockSys(
    override val routeSys: Route.Sys = RouteSys(),
    override val indicatorSys: Indicator.Sys = IndicatorSys(),
    override val notificationSys: Notification.Sys = NotificationSys(),
    override val clipboardSys: Clip.Board.Sys = ClipBoardSys(),
    override val networkSys: Network.Sys = NetworkSys()
) : Sys {
    override fun createRouteSys(): Route.Sys = RouteSys()
}
