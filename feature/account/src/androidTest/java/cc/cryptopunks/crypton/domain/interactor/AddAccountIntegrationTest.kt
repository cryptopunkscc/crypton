package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.IntegrationTest
import cc.cryptopunks.crypton.entity.Account.Status.Connected
import org.junit.Assert.assertEquals
import org.junit.Test

class AddAccountIntegrationTest : IntegrationTest() {

    override fun setUp(): Unit = with(client1) {
        connect()
        create()
        disconnect()
    }

    @Test
    fun invoke(): Unit = with(component) {
        // given
        val id = 1L
        val account = account(id)
        val expected = account.copy(
            id = id,
            status = Connected
        )

        // when
        val addition = addAccount(account).test()

        // then
        addition
            .assertNoErrors()
            .assertComplete()

        assertEquals(
            expected,
            accountDao.get(id)
        )
    }

    override fun tearDown(): Unit = with(client1) {
        connect()
        login()
        remove()
        super.tearDown()
    }
}


