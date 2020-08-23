package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Device
import kotlinx.coroutines.flow.Flow

class MockDeviceNet : Device.Net {

    override fun trust(fingerprint: Device.Fingerprint) {
        TODO("Not yet implemented")
    }

    override fun distrust(fingerprint: Device.Fingerprint) {
        TODO("Not yet implemented")
    }

    override fun setDeviceFingerprintRepo(repo: Device.Repo) {
        TODO("Not yet implemented")
    }

    override fun listActiveFingerprints(address: Address): List<Device.Fingerprint> {
        TODO("Not yet implemented")
    }

    override fun fingerprintStateChangedFlow(): Flow<Device.Fingerprint> {
        TODO("Not yet implemented")
    }

    override fun purgeDeviceList() {
        TODO("Not yet implemented")
    }
}
