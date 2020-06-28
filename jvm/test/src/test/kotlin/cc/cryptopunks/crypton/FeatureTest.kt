package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.feature.testDirectMessaging
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

class FeatureTest {

    @Test
    fun `as a user I can use direct messages`() = runBlocking {
        testDirectMessaging()
    }

    @After
    fun tearDown() = runBlocking {
        connectClient {
            removeAccounts(
                address1,
                address2,
                address3
            )
        }
    }

    companion object {
        private val server = TestServer()

        @BeforeClass
        @JvmStatic
        fun beforeAll() = server.start()

        @AfterClass
        @JvmStatic
        fun afterAll() = server.stop()
    }
}
