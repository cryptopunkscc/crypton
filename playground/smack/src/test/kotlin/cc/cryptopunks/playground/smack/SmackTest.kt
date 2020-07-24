package cc.cryptopunks.playground.smack

import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.roster.Roster
import org.jivesoftware.smack.roster.SubscribeListener
import org.jivesoftware.smackx.iqregister.AccountManager
import org.jivesoftware.smackx.muc.MultiUserChatManager
import org.jivesoftware.smackx.omemo.OmemoManager
import org.junit.Ignore
import org.junit.Test
import org.jxmpp.jid.parts.Localpart
import org.jxmpp.jid.parts.Resourcepart


class SmackTest {

    @Test
    @Ignore
    fun test() {
        runBlocking {
            initOmemo()
            listOf(
                launch { client1() },
                launch { client2() }
            ).joinAll()
        }
    }
}

private suspend fun client1() {
    createXmppConnection {
        setUsernameAndPassword(test1, password)
    }.run {
            replyTimeout = 10000 // Omemo initialization can take longer then 5s
            fromMode = XMPPConnection.FromMode.USER

            addConnectionListener(TestConnectionListener(address1))

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
            accountManager.createAccount(Localpart.from(test1), password)
            login()
            println("successful login $address1")

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
    createXmppConnection {
        setUsernameAndPassword(test2, "pass")
    }.run {
            replyTimeout = 10000 // Omemo initialization can take longer then 5s
            fromMode = XMPPConnection.FromMode.USER

            addConnectionListener(TestConnectionListener(address2))

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
            println("successful login $address2")

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
