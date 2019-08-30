package cc.cryptopunks.crypton.account.domain.interactor

import cc.cryptopunks.crypton.account.IntegrationTest
import cc.cryptopunks.crypton.entity.Account.Status.Connected
import org.junit.Assert.assertEquals
import org.junit.Test

class CreateAccountIntegrationTest : IntegrationTest() {

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
        val creation = createAccount(account).test()

        // then
        creation
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