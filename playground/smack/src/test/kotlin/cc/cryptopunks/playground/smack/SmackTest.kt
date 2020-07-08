package cc.cryptopunks.playground.smack

import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.ConnectionListener
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.roster.Roster
import org.jivesoftware.smack.roster.SubscribeListener
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.iqregister.AccountManager
import org.jivesoftware.smackx.muc.MultiUserChatManager
import org.jivesoftware.smackx.omemo.OmemoConfiguration
import org.jivesoftware.smackx.omemo.OmemoManager
import org.jivesoftware.smackx.omemo.internal.OmemoDevice
import org.jivesoftware.smackx.omemo.signal.SignalCachingOmemoStore
import org.jivesoftware.smackx.omemo.signal.SignalFileBasedOmemoStore
import org.jivesoftware.smackx.omemo.signal.SignalOmemoService
import org.jivesoftware.smackx.omemo.trust.OmemoFingerprint
import org.jivesoftware.smackx.omemo.trust.OmemoTrustCallback
import org.jivesoftware.smackx.omemo.trust.TrustState
import org.junit.Test
import org.jxmpp.jid.impl.JidCreate
import org.jxmpp.jid.parts.Localpart
import org.jxmpp.jid.parts.Resourcepart
import java.io.File
import java.lang.Exception
import java.net.InetAddress

private const val test1 = "test11"
private const val test2 = "test12"
private const val domain = "localhost"
private val address1 = JidCreate.bareFrom("$test1@$domain")
private val address2 = JidCreate.bareFrom("$test1@$domain")
private val chat = JidCreate.entityBareFrom("chat@conference.$domain")

private const val omemoStore = "omemo_store"

class SmackTest {

    @Test
    fun test() {
        runBlocking {
            init()
            listOf(
                launch { client1() },
                launch { client2() }
            ).joinAll()
        }
    }
}

private fun init() {
    OmemoConfiguration.setRepairBrokenSessionsWithPrekeyMessages(true)
    SignalOmemoService.acknowledgeLicense()
    SignalOmemoService.setup()
    SignalOmemoService.getInstance().apply {
        omemoStoreBackend = SignalCachingOmemoStore(SignalFileBasedOmemoStore(File(omemoStore)))
    }
}

private suspend fun client1() {
    XMPPTCPConnectionConfiguration.builder()
        .enableDefaultDebugger()
        .setHostAddress(InetAddress.getByName("127.0.0.1"))
        .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
        .setUsernameAndPassword(test1, "pass")
        .setXmppDomain("localhost")
        .build()
        .let(::XMPPTCPConnection)
        .run {
            replyTimeout = 10000 // Omemo initialization can take longer then 5s
            fromMode = XMPPConnection.FromMode.USER

            addConnectionListener(object : ConnectionListener {
                override fun connected(connection: XMPPConnection?) {

                }

                override fun connectionClosed() {
                }

                override fun connectionClosedOnError(e: Exception?) {
                }

                override fun authenticated(connection: XMPPConnection?, resumed: Boolean) {

                }
            })

            // delete account
            connect()
            try {
                login()
                AccountManager.getInstance(this).deleteAccount()
            } catch (e: Throwable) {
                println("No account to clear")
            } finally {
                try {
                    disconnect()
                } catch (e: Throwable) {
                }
            }

            // create account
            connect()
            val accountManager = AccountManager.getInstance(this).apply {
                sensitiveOperationOverInsecureConnection(true)
            }
            accountManager.createAccount(Localpart.from(test1), "pass")
            login()

            // add listeners
            Roster.getInstanceFor(this).addSubscribeListener { from, subscribeRequest ->
                SubscribeListener.SubscribeAnswer.Approve
            }

            // init omemo
            OmemoManager.getInstanceFor(this).apply {
                setTrustCallback(OmemoTrustAllCallback)
                initialize()
            }

            // subscribe
            Roster.getInstanceFor(this).sendSubscriptionRequest(address2)


            // wait for accept
            while (!Roster.getInstanceFor(this).iAmSubscribedTo(address2)) delay(500)

            // create chat
            MultiUserChatManager.getInstanceFor(this).getMultiUserChat(chat).apply {

                // create reserved muc
                createOrJoin(Resourcepart.from(test1))

                // configure
                configurationForm.fields.forEachIndexed { index, formField ->
                    println(formField.toXML("$index"))
                }
                sendConfigurationForm(
                    configurationForm.createAnswerForm().apply {
                        setAnswer("muc#roomconfig_membersonly", true)
                        setAnswer("muc#roomconfig_publicroom", false)
                        setAnswer("muc#roomconfig_allowinvites", true)
                        setAnswer("muc#roomconfig_persistentroom", true)
                    }
                )

                // invite users
                invite(address2.asEntityBareJidOrThrow(), "")
            }

            // delete account
            accountManager.deleteAccount()
            disconnect()
        }
}

private suspend fun client2() {
    XMPPTCPConnectionConfiguration.builder()
        .enableDefaultDebugger()
        .setHostAddress(InetAddress.getByName("127.0.0.1"))
        .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
        .setUsernameAndPassword(test2, "pass")
        .setXmppDomain("localhost")
        .build()
        .let(::XMPPTCPConnection)
        .run {
            replyTimeout = 10000 // Omemo initialization can take longer then 5s
            fromMode = XMPPConnection.FromMode.USER

            // delete account
            connect()
            try {
                login()
                AccountManager.getInstance(this).deleteAccount()
            } catch (e: Throwable) {
                println("No account to clear")
            } finally {
                try {
                    disconnect()
                } catch (e: Throwable) {
                }
            }

            // add listeners
            Roster.getInstanceFor(this).addSubscribeListener { from, subscribeRequest ->
                Roster.getInstanceFor(this).sendSubscriptionRequest(address1)
                SubscribeListener.SubscribeAnswer.Approve
            }

            // create account
            connect()
            val accountManager = AccountManager.getInstance(this).apply {
                sensitiveOperationOverInsecureConnection(true)
            }
            accountManager.createAccount(Localpart.from(test2), "pass")
            login()

            // init omemo
            OmemoManager.getInstanceFor(this).apply {
                setTrustCallback(OmemoTrustAllCallback)
                initialize()
            }

            // delete account
            accountManager.deleteAccount()
            disconnect()
        }
}


private object OmemoTrustAllCallback :
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
