package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.IntegrationTest
import cc.cryptopunks.crypton.entity.Account.Status.Connected
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class ConnectAccountIntegrationTest : IntegrationTest() {

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
            val account = account(address(1))
            val expected = account.copy(
                status = Connected
            )

            // when
            connectAccount(account).join()

            // then
            assertEquals(
                expected,
                accountRepo.get(account.address)
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