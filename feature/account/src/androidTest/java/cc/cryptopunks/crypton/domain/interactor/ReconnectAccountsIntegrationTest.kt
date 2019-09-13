package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.IntegrationTest
import cc.cryptopunks.crypton.entity.Account.Status.Connected
import cc.cryptopunks.crypton.entity.Account.Status.Connecting
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class ReconnectAccountsIntegrationTest : IntegrationTest() {

    private val ids = 1..3

    override fun setUp() {
        clients(ids).forEach {
            with(it) {
                insertAccount { copy(status = Connecting) }
                connect()
                create()
                disconnect()
            }
        }
    }

    @Test
    fun invoke(): Unit = with(component) {
        runBlocking {
            // given
            val expected = ids.map {
                account(
                    index = it.toLong(),
                    withId = true
                ).copy(
                    status = Connected
                )
            }

            // when
            reconnectAccounts().join()

            // then
            assertEquals(
                expected,
                accountDao.list()
            )
        }
    }

    override fun tearDown() {
        clients(ids).forEach {
            with(it) {
                connect()
                login()
                remove()
            }
        }
        super.tearDown()
    }
}