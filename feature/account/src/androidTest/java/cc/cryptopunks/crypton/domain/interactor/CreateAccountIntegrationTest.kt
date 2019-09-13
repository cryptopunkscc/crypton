package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.IntegrationTest
import cc.cryptopunks.crypton.entity.Account.Status.Connected
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class CreateAccountIntegrationTest : IntegrationTest() {

    @Test
    fun invoke(): Unit = with(component) {
        runBlocking {
            // given
            val id = 1L
            val account = account(id)
            val expected = account.copy(
                id = id,
                status = Connected
            )

            // when
            createAccount(account).join()

            // then
            assertEquals(
                expected,
                accountDao.get(id)
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