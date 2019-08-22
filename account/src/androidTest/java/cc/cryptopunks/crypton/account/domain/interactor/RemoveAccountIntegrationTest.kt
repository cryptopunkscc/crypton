package cc.cryptopunks.crypton.account.domain.interactor

import cc.cryptopunks.crypton.account.IntegrationTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RemoveAccountIntegrationTest : IntegrationTest() {

    override fun setUp(): Unit = with(xmpp1) {
        insertAccount()
    }

    @Test
    fun invoke(): Unit = with(component) {
        // given
        val id = 1L
        val expected = null

        // when
        val connection = removeAccount(id).test()

        // then
        connection
            .assertComplete()
            .assertNoErrors()

        assertEquals(
            expected,
            accountDao.contains(id)
        )

        assertNull(
            xmppCache[id]
        )
    }
}