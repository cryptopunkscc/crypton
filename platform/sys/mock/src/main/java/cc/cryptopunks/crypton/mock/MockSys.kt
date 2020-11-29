package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.AesGcm
import cc.cryptopunks.crypton.context.Clip
import cc.cryptopunks.crypton.context.Device
import cc.cryptopunks.crypton.context.Execute
import cc.cryptopunks.crypton.context.Indicator
import cc.cryptopunks.crypton.context.Network
import cc.cryptopunks.crypton.context.Notification
import cc.cryptopunks.crypton.context.Sys
import cc.cryptopunks.crypton.context.URI

class MockSys(
    override val indicatorSys: Indicator.Sys = MockIndicatorSys(),
    override val notificationSys: Notification.Sys = MockNotificationSys(),
    override val clipboardSys: Clip.Board.Sys = MockClipBoardSys(),
    override val networkSys: Network.Sys = MockNetworkSys(),
    override val deviceSys: Device.Sys = MockDeviceSys(),
    override val executeSys: Execute.Sys = MockExecuteSys,
    override val uriSys: URI.Sys = MockURISys,
    override val aesGcmSys: AesGcm.Sys = MockAesGcmSys,
) : Sys
