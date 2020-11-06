package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.Device

class MockDeviceSys : Device.Sys {
    override fun deviceId(): String = "MOCK_DEVICE_ID"
}
