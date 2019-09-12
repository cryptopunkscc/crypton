package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.IntegrationTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DeleteAccountIntegrationTest : IntegrationTest() {

    override fun setUp(): Unit = with(client1) {
        insertAccount()
        connect()
        create()
        disconnect()
    }

    @Test
    fun invoke(): Unit = with(component) {
        // given
        val id = 1L
        val expected = null
        connectAccount(id).blockingAwait()

        // when
        val connection = deleteAccount(id).test()

        // then
        connection
            .assertComplete()
            .assertNoErrors()

        assertEquals(
            expected,
            accountDao.contains(id)
        )

        assertNull(
            clientCache[id]
        )
    }
}