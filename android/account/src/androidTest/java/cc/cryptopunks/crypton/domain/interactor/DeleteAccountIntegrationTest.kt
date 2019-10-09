package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.IntegrationTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DeleteAccountIntegrationTest : IntegrationTest() {

    override fun setUp(): Unit = with(client1) {
        insertAccount()
    }

    @Test
    fun invoke(): Unit = with(component) {
        runBlocking {
            // given
            val account = account(address(1))
            val expected = null

            // when
            deleteAccount(account).join()

            // then
            assertEquals(
                expected,
                accountRepo.contains(account.address)
            )

            assertNull(
                clientCache[account.address.id]
            )
        }
    }
}