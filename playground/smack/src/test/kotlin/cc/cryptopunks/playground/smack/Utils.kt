package cc.cryptopunks.playground.smack

import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.ConnectionListener
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.omemo.OmemoConfiguration
import org.jivesoftware.smackx.omemo.internal.OmemoDevice
import org.jivesoftware.smackx.omemo.signal.SignalCachingOmemoStore
import org.jivesoftware.smackx.omemo.signal.SignalFileBasedOmemoStore
import org.jivesoftware.smackx.omemo.signal.SignalOmemoService
import org.jivesoftware.smackx.omemo.trust.OmemoFingerprint
import org.jivesoftware.smackx.omemo.trust.OmemoTrustCallback
import org.jivesoftware.smackx.omemo.trust.TrustState
import org.jxmpp.jid.impl.JidCreate
import java.io.File
import java.lang.Exception
import java.net.InetAddress


const val test1 = "test11"
const val test2 = "test12"
const val domain = "localhost"
const val password = "pass"
val address1 = JidCreate.bareFrom("$test1@$domain")
val address2 = JidCreate.bareFrom("$test2@$domain")
val chat = JidCreate.entityBareFrom("chat@conference.$domain")

private const val omemoStore = "omemo_store"

fun initOmemo() {
    OmemoConfiguration.setRepairBrokenSessionsWithPrekeyMessages(true)
    SignalOmemoService.acknowledgeLicense()
    SignalOmemoService.setup()
    SignalOmemoService.getInstance().apply {
        omemoStoreBackend = SignalCachingOmemoStore(SignalFileBasedOmemoStore(File(omemoStore)))
    }
}

fun createXmppConnection(build: XMPPTCPConnectionConfiguration.Builder.() -> Unit) =
    XMPPTCPConnectionConfiguration.builder()
        .enableDefaultDebugger()
        .setHostAddress(InetAddress.getByName("127.0.0.1"))
        .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
        .setXmppDomain("localhost")
        .apply(build)
        .build()
        .let(::XMPPTCPConnection)
        .apply {
            replyTimeout = 10000 // Omemo initialization can take longer then 5s
            fromMode = XMPPConnection.FromMode.USER
        }

class TestConnectionListener(val address: Any) : ConnectionListener {
    override fun connected(connection: XMPPConnection?) {
        println("connected $address")
    }

    override fun connectionClosed() {
        println("connectionClosed $address")
    }

    override fun connectionClosedOnError(e: Exception) {
        println("connectionClosedOnError $address ${e.message}")
    }

    override fun authenticated(connection: XMPPConnection?, resumed: Boolean) {
        println("authenticated $address resumed: $resumed")
    }
}

object OmemoTrustAllCallback :
    OmemoTrustCallback {

    override fun setTrust(
        device: OmemoDevice,
        fingerprint: OmemoFingerprint,
        state: TrustState
    ) = Unit

    override fun getTrust(
        device: OmemoDevice,
        fingerprint: OmemoFingerprint
    ) = TrustState.trusted
}
