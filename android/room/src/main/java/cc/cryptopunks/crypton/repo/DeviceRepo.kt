package cc.cryptopunks.crypton.repo

import cc.cryptopunks.crypton.context.Device
import cc.cryptopunks.crypton.context.address
import cc.cryptopunks.crypton.entity.FingerprintData

internal class DeviceRepo(
    private val dao: FingerprintData.Dao
) : Device.Repo {
    override suspend fun set(fingerprint: Device.Fingerprint) {
        dao.insert(fingerprint.toData())
    }

    override suspend fun get(value: CharSequence): Device.Fingerprint? =
        dao.get(value.toString())?.toDomain()

    override suspend fun clear() {
        dao.clear()
    }
}

private fun Device.Fingerprint.toData() = FingerprintData(
    fingerprint = value.toString(),
    deviceId = device.id,
    address = device.address.id,
    state = state.name
)

private fun FingerprintData.toDomain() = Device.Fingerprint(
    value = fingerprint,
    state = Device.TrustState.valueOf(state),
    device = Device(
        id = deviceId,
        address = address(address)
    )
)
