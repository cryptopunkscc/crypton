package cc.cryptopunks.crypton.smack.integration

import cc.cryptopunks.crypton.net.Client
import org.jivesoftware.smack.util.stringencoder.Base64
import org.jivesoftware.smack.util.stringencoder.java7.Java7Base64Encoder
import org.junit.After
import org.junit.Before

abstract class IntegrationTest : ApiIntegrationTest() {

    private var counter = 0

    init {
        staticInit
    }

    @Before
    override fun setUp() {
        createClient {
            copy(
                resource = "xmpptest${counter++}",
                hostAddress = "127.0.0.1",
                securityMode = Client.Factory.Config.SecurityMode.disabled
            )
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