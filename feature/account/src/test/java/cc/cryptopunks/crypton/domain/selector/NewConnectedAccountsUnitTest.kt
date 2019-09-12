package cc.cryptopunks.crypton.domain.selector

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Account.Status.Connected
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class NewConnectedAccountsUnitTest: CoroutineScope by CoroutineScope(Dispatchers.Unconfined) {

    @RelaxedMockK
    lateinit var dao: Account.Dao
    private lateinit var newAccountConnected: NewAccountConnected

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        newAccountConnected = NewAccountConnected(dao)
    }

    @Test
    operator fun invoke() {
        runBlocking {
            // given
            every { dao.flowList() } returns flow<List<Account>> {
                emit(
                    emptyList()
                )
                emit(
                    listOf(
                        Account(id = 1, status = Connected),
                        Account(id = 2)
                    )
                )
                emit(
                    listOf(
                        Account(id = 1, status = Connected),
                        Account(id = 2, status = Connected)
                    )
                )
                emit(
                    listOf(
                        Account(id = 1)
                    )
                )
            }

            // when
            val addedAccounts = newAccountConnected()

            // then
            assertEquals(
                listOf(1L, 2L),
                addedAccounts.toList(mutableListOf())
            )
        }
    }
}