package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.Device

class MockDeviceRepo : Device.Repo {

    override suspend fun set(fingerprint: Device.Fingerprint) {
        TODO("Not yet implemented")
    }

    override suspend fun get(value: CharSequence): Device.Fingerprint? {
        TODO("Not yet implemented")
    }

    override suspend fun clear() {
        TODO("Not yet implemented")
    }
}
