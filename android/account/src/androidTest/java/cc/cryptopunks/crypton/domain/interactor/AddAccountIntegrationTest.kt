package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.IntegrationTest
import cc.cryptopunks.crypton.entity.Account.Status.Connected
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class AddAccountIntegrationTest : IntegrationTest() {

    override fun setUp(): Unit = with(client1) {
        connect()
        createAccount()
        disconnect()
    }

    @Test
    fun invoke(): Unit = with(component) {
        runBlocking {
            // given
            val id = address(1)
            val account = account(id)
            val expected = account.copy(
                address = id,
                status = Connected
            )

            // when
            addAccount(account).join()

            // then
            assertEquals(
                expected,
                accountRepo.get(id)
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


