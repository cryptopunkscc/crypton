package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.IntegrationTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class UnregisterAccountIntegrationTest : IntegrationTest() {

    override fun setUp(): Unit = with(client1) {
        insertAccount()
        connect()
        createAccount()
        disconnect()
    }

    @Test
    fun invoke(): Unit = with(component) {
        runBlocking {
            // given
            val account = account(address(1))
            val expected = null
            connectAccount(account).join()

            // when
            unregisterAccount(account).join()

            // then
            assertEquals(
                expected,
                accountRepo.contains(account.address)
            )

            assertNull(
                clientManager[account]
            )
        }
    }
}