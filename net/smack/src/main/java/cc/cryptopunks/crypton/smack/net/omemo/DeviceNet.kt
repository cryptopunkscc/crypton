package cc.cryptopunks.crypton.smack.net.omemo

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Device
import cc.cryptopunks.crypton.context.Device.TrustState.Trusted
import cc.cryptopunks.crypton.context.Device.TrustState.Undecided
import cc.cryptopunks.crypton.context.Device.TrustState.Unset
import cc.cryptopunks.crypton.context.Device.TrustState.valueOf
import cc.cryptopunks.crypton.smack.SmackCore
import cc.cryptopunks.crypton.smack.util.address
import cc.cryptopunks.crypton.smack.util.entityBareJid
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jivesoftware.smackx.omemo.internal.OmemoDevice
import org.jivesoftware.smackx.omemo.trust.OmemoFingerprint
import org.jivesoftware.smackx.omemo.trust.OmemoTrustCallback
import org.jivesoftware.smackx.omemo.trust.TrustState
import java.util.*

internal class DeviceNet(
    core: SmackCore
) : Device.Net,
    OmemoTrustCallback,
    SmackCore by core {

    private val deviceChangesChannel = ConflatedBroadcastChannel<Device.Fingerprint>()
    private lateinit var deviceRepo: Device.Repo

    override fun setDeviceFingerprintRepo(repo: Device.Repo) {
        deviceRepo = repo
    }

    override fun listActiveFingerprints(address: Address): List<Device.Fingerprint> = omemoManager
        .getActiveFingerprints(address.entityBareJid())
        .toDeviceFingerprints()


    override fun fingerprintStateChangedFlow(): Flow<Device.Fingerprint> =
        deviceChangesChannel.asFlow()

    override fun trust(fingerprint: Device.Fingerprint) {
        omemoManager.trustOmemoIdentity(
            fingerprint.device.toOmemoDevice(),
            fingerprint.toOmemoFingerprint()
        )
    }

    override fun distrust(fingerprint: Device.Fingerprint) {
        omemoManager.distrustOmemoIdentity(
            fingerprint.device.toOmemoDevice(),
            fingerprint.toOmemoFingerprint()
        )
    }

    override fun setTrust(
        device: OmemoDevice,
        fingerprint: OmemoFingerprint,
        state: TrustState?
    ) {
        launch { deviceRepo.set(deviceFingerprint(device, fingerprint, state)) }
    }

    override fun getTrust(
        device: OmemoDevice,
        fingerprint: OmemoFingerprint
    ): TrustState? = runBlocking {
        deviceRepo.get(fingerprint)
            ?.state?.toOmemoTrustState()
            ?: TrustState.trusted
    }

    override fun purgeDeviceList() {
        omemoManager.purgeDeviceList()
    }
}

private fun Device.TrustState.toOmemoTrustState() =
    when (this) {
        Undecided -> Trusted
        Unset -> Undecided
        else -> this
    }.name.toLowerCase(Locale.ROOT).let {
        TrustState.valueOf(it)
    }

private fun Device.Fingerprint.toOmemoFingerprint() = OmemoFingerprint(value.toString())

private fun Device.toOmemoDevice() = OmemoDevice(address.entityBareJid(), id)

private fun Map<OmemoDevice, OmemoFingerprint>.toDeviceFingerprints() =
    map { (device: OmemoDevice, fingerprint: OmemoFingerprint) ->
        deviceFingerprint(device, fingerprint)
    }

private fun deviceFingerprint(
    device: OmemoDevice,
    fingerprint: OmemoFingerprint,
    state: TrustState? = null
) = Device.Fingerprint(
    value = fingerprint,
    state = state
        ?.run { valueOf(name.capitalize()) }
        ?: Unset,
    device = Device(
        id = device.deviceId,
        address = device.jid.address()
    )
)
