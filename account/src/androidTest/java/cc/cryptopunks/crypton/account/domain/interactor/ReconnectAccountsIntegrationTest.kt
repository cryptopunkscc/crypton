package cc.cryptopunks.crypton.account.domain.interactor

import cc.cryptopunks.crypton.account.IntegrationTest
import cc.cryptopunks.crypton.core.entity.Account.Status.Connected
import cc.cryptopunks.crypton.core.entity.Account.Status.Connecting
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.TimeUnit

class ReconnectAccountsIntegrationTest : IntegrationTest() {

    private val ids = 1..3

    override fun setUp() {
        xmpps(ids).forEach {
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
        val reconnection = reconnectAccounts().test()

        // then
        reconnection.awaitTerminalEvent(5, TimeUnit.SECONDS)

        assertEquals(
            expected,
            accountDao.getAll()
        )
    }

    override fun tearDown() {
        xmpps(ids).forEach {
            with(it) {
                connect()
                login()
                remove()
            }
        }
        super.tearDown()
    }
}