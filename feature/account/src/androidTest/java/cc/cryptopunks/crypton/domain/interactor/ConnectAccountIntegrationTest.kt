package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.IntegrationTest
import cc.cryptopunks.crypton.entity.Account.Status.Connected
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class ConnectAccountIntegrationTest : IntegrationTest() {

    override fun setUp(): Unit = with(client(1L)) {
        insertAccount()
        connect()
        create()
        disconnect()
    }

    @Test
    fun invoke(): Unit = with(component) {
        runBlocking {
            // given
            val id = 1L
            val expected = account(id).copy(
                id = id,
                status = Connected
            )

            // when
            connectAccount(id).join()

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