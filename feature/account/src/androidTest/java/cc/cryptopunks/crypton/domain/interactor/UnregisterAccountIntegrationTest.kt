package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.IntegrationTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class UnregisterAccountIntegrationTest : IntegrationTest() {

    override fun setUp(): Unit = with(client1) {
        insertAccount()
        connect()
        create()
        disconnect()
    }

    @Test
    fun invoke(): Unit = with(component) {
        runBlocking {
            // given
            val account = account(1)
            val expected = null
            connectAccount(account).join()

            // when
            unregisterAccount(account).join()

            // then
            assertEquals(
                expected,
                accountDao.contains(account.id)
            )

            assertNull(
                clientCache[account.id]
            )
        }
    }
}