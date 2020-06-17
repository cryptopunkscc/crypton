package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.Clip
import cc.cryptopunks.crypton.context.Indicator
import cc.cryptopunks.crypton.context.Network
import cc.cryptopunks.crypton.context.Notification
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.Sys

class MockSys(
    override val indicatorSys: Indicator.Sys = MockIndicatorSys(),
    override val notificationSys: Notification.Sys = MockNotificationSys(),
    override val clipboardSys: Clip.Board.Sys = MockClipBoardSys(),
    override val networkSys: Network.Sys = MockNetworkSys(),
    override val createRouteSys: () -> Route.Sys = { MockRouteSys() }
) : Sys {
    override val routeSys: Route.Sys = createRouteSys()
}
