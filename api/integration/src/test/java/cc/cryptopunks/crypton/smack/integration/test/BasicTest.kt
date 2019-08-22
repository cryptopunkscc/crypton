package cc.cryptopunks.crypton.smack.integration.test

import cc.cryptopunks.crypton.smack.integration.IntegrationTest
import org.junit.Assert.assertTrue
import org.junit.Test

internal class BasicTest : IntegrationTest() {

    @Test
    operator fun invoke() {
        with(client1) {
            create()
            disconnect()
            connect()
            login()
            assertTrue(isAuthenticated())
            remove()
        }
    }
}