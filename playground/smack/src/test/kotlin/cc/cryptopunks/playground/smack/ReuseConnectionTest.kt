package cc.cryptopunks.playground.smack

import org.jivesoftware.smackx.iqregister.AccountManager
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.jxmpp.jid.parts.Localpart

class ReuseConnectionTest {

    companion object {
        @JvmStatic
        @BeforeClass
        fun setupOmemo() {
            initOmemo()
        }
    }

    /**
     * Is not possible to reuse connection after delete account.
     * The resource of last iq and query is different from others.
     * @see [ReuseConnectionTest_test1.xml]
     * @see [ReuseConnectionTest_test1.log]
     * @see <a href="https://xmpp.org/rfcs/rfc6120.html#streams-error-conditions-invalid-from">invalid-from</a>
     */
    @Test(expected = AssertionError::class)
    fun clientWithoutCustomResource() {
        createXmppConnection {
            setUsernameAndPassword(test1, password)
        }.run {
            addConnectionListener(TestConnectionListener(address1))

            // delete account
            connect()
            try {
                login()
                println("delete account $address1")
                AccountManager.getInstance(this).deleteAccount()
            } catch (e: Throwable) {
                println("No account to clear")
            } finally {
                try {
                    println("try disconnect")
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
            Thread.sleep(2000)
            Assert.assertTrue(isConnected)
        }
    }

    /**
     * Is not possible to reuse connection after delete account.
     * @see [ReuseConnectionTest_test2.xml]
     * @see [ReuseConnectionTest_test2.log]
     * @see <a href="https://xmpp.org/rfcs/rfc6120.html#streams-error-conditions-conflict">conflict</a>
     */
    @Test(expected = AssertionError::class)
    fun clientWithCustomResource() {
        createXmppConnection {
            setUsernameAndPassword(test1, password)
            setResource("testresource")
        }.run {
            addConnectionListener(TestConnectionListener(address1))

            // delete account
            connect()
            try {
                login()
                println("delete account $address1")
                AccountManager.getInstance(this).deleteAccount()
            } catch (e: Throwable) {
                println("No account to clear")
            } finally {
                try {
                    println("try disconnect")
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
            Thread.sleep(2000)
            Assert.assertTrue(isConnected)
        }
    }
}
