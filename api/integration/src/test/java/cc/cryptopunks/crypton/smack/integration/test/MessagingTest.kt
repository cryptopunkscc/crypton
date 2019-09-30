package cc.cryptopunks.crypton.smack.integration.test

import cc.cryptopunks.crypton.smack.integration.IntegrationTest
import cc.cryptopunks.crypton.smack.integration.test
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import org.junit.Assert.assertEquals
import org.junit.Test

internal class MessagingTest : IntegrationTest() {

    override fun init() {
        clients(1..2)
    }

    @Test
    fun invoke() = test {
        val expected = "test"

        val actual = async {
            client2.messagePublisher
                .asFlow()
                .first()
                .text
        }

        launch {
            client1.sendMessage(
                client2.address,
                expected
            )
        }

        assertEquals(
            expected,
            actual.await()
        )
    }
}