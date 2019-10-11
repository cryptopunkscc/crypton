package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.IntegrationTest
import cc.cryptopunks.crypton.entity.Account.Status.Disconnected
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DisconnectAccountIntegrationTest : IntegrationTest() {

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
            val expected = account.copy(
                status = Disconnected
            )

            // when
            disconnectAccount(account).join()

            // then
            assertEquals(
                expected,
                accountRepo.get(account.address)
            )

            assertNull(
                clientManager[account]
            )
        }
    }

    override fun tearDown(): Unit = with(client1) {
        connect()
        login()
        remove()
        super.tearDown()
    }
}