package cc.cryptopunks.crypton.domain.selector

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Account.Status.Connected
import cc.cryptopunks.crypton.feature.account.selector.NewAccountConnected
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
    lateinit var repo: Account.Repo
    private lateinit var newAccountConnected: NewAccountConnected

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        newAccountConnected = NewAccountConnected(repo)
    }

    @Test
    operator fun invoke() {
        runBlocking {
            // given
            every { repo.flowList() } returns flow<List<Account>> {
                emit(
                    emptyList()
                )
                emit(
                    listOf(
                        Account(address = 1, status = Connected),
                        Account(address = 2)
                    )
                )
                emit(
                    listOf(
                        Account(address = 1, status = Connected),
                        Account(address = 2, status = Connected)
                    )
                )
                emit(
                    listOf(
                        Account(address = 1)
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