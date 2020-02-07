package cc.cryptopunks.crypton.smack.net.omemo

import org.jivesoftware.smackx.omemo.internal.OmemoDevice
import org.jivesoftware.smackx.omemo.trust.OmemoFingerprint
import org.jivesoftware.smackx.omemo.trust.OmemoTrustCallback
import org.jivesoftware.smackx.omemo.trust.TrustState

object OmemoTrustAllCallback:
    OmemoTrustCallback {

    override fun setTrust(
        device: OmemoDevice,
        fingerprint: OmemoFingerprint,
        state: TrustState
    ) = Unit

    override fun getTrust(
        device: OmemoDevice,
        fingerprint: OmemoFingerprint
    ): TrustState =
        TrustState.trusted

}