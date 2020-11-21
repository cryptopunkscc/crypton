package cc.cryptopunks.crypton.sys

import cc.cryptopunks.crypton.context.Clip
import cc.cryptopunks.crypton.context.Device
import cc.cryptopunks.crypton.context.Execute
import cc.cryptopunks.crypton.context.Indicator
import cc.cryptopunks.crypton.context.Network
import cc.cryptopunks.crypton.context.Notification
import cc.cryptopunks.crypton.context.Sys
import cc.cryptopunks.crypton.mock.MockClipBoardSys
import cc.cryptopunks.crypton.mock.MockDeviceSys
import cc.cryptopunks.crypton.mock.MockIndicatorSys
import cc.cryptopunks.crypton.mock.MockNotificationSys
import kotlinx.coroutines.GlobalScope

class JvmSys(
    override val indicatorSys: Indicator.Sys = MockIndicatorSys(),
    override val notificationSys: Notification.Sys = MockNotificationSys(),
    override val clipboardSys: Clip.Board.Sys = MockClipBoardSys(),
    override val networkSys: Network.Sys = JvmNetworkSys(GlobalScope),
    override val deviceSys: Device.Sys = MockDeviceSys(),
    override val executeSys: Execute.Sys = JvmExecuteSys
) : Sys
