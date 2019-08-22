package cc.cryptopunks.crypton.smack.integration

import cc.cryptopunks.crypton.smack.SmackClientFactory
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.util.stringencoder.Base64
import org.jivesoftware.smack.util.stringencoder.java7.Java7Base64Encoder
import org.junit.After
import org.junit.Before
import java.net.InetAddress

abstract class IntegrationTest : ApiIntegrationTest() {

    private var counter = 0

    init {
        staticInit
    }

    @Before
    override fun setUp() {
        SmackClientFactory {
            setResource("xmpptest${counter++}")
            setHostAddress(InetAddress.getLocalHost())
            setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
        }
        super.setUp()
    }

    @After
    override fun tearDown() = super.tearDown()

    companion object {
        private val staticInit by lazy {
            Base64.setEncoder(Java7Base64Encoder.getInstance())
        }
    }
}