package cc.cryptopunks.crypton.core.interactor

import cc.cryptopunks.crypton.core.IntegrationTest
import cc.cryptopunks.crypton.core.entity.Account.Status.Disconnected
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DisconnectAccountIntegrationTest : IntegrationTest() {

    override fun setUp(): Unit = with(xmpp1) {
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
            status = Disconnected
        )

        // when
        val connection = disconnectAccount(id).test()

        // then
        connection
            .assertComplete()
            .assertNoErrors()

        assertEquals(
            expected,
            accountDao.get(id)
        )

        assertNull(
            xmppCache[id]
        )
    }

    override fun tearDown(): Unit = with(xmpp1) {
        connect()
        login()
        remove()
        super.tearDown()
    }
}