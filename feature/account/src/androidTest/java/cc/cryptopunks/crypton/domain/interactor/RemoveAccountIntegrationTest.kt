package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.IntegrationTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RemoveAccountIntegrationTest : IntegrationTest() {

    override fun setUp(): Unit = with(client1) {
        insertAccount()
    }

    @Test
    fun invoke(): Unit = with(component) {
        runBlocking {
            // given
            val id = 1L
            val expected = null

            // when
            removeAccount(id).join()

            // then
            assertEquals(
                expected,
                accountDao.contains(id)
            )

            assertNull(
                clientCache[id]
            )
        }
    }
}