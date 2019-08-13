package cc.cryptopunks.crypton.core.interactor

import cc.cryptopunks.crypton.core.IntegrationTest
import cc.cryptopunks.crypton.core.entity.Account.Status.Connected
import org.junit.Assert.assertEquals
import org.junit.Test

class ConnectAccountIntegrationTest : IntegrationTest() {

    override fun setUp(): Unit = with(xmpp(1L)) {
        insertAccount()
        connect()
        create()
        disconnect()
    }

    @Test
    fun invoke(): Unit = with(component) {
        // given
        val id = 1L
        val expected = account(id).copy(
            id = id,
            status = Connected
        )

        // when
        val connection = connectAccount(id).test()

        // then
        connection
            .assertComplete()
            .assertNoErrors()

        assertEquals(
            expected,
            accountDao.get(id)
        )
    }

    override fun tearDown(): Unit = with(xmpp1) {
        connect()
        login()
        remove()
        super.tearDown()
    }
}