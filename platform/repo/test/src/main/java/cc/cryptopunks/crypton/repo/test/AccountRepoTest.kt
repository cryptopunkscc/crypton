package cc.cryptopunks.crypton.repo.test

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Password
import cc.cryptopunks.crypton.context.address
import cc.cryptopunks.crypton.repo.AccountRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

abstract class AccountRepoTest {

    abstract val repo: AccountRepo

    private val address = address("test@address")

    private val account = Account(
        address = address("test@address"),
        enabled = true,
        password = Password("test")
    )

    @Test
    fun insertContains() = runBlocking {
        repo.run {
            // when
            insert(account)

            // then
            assertTrue(contains(address))
        }
    }

    @Test
    fun insertGet() = runBlocking {
        repo.run {
            // when
            insert(account)

            // then
            assertEquals(account, get(address))
        }
    }

    @Test
    fun insertUpdateGet() = runBlocking {
        repo.run {
            // when
            insert(account)

            val new = account.copy(password = Password("test2"))

            update(new)

            // then
            assertEquals(new, get(address))
        }
    }

    @Test
    fun insertContainsDeleteContains() = runBlocking {
        repo.run {
            // when
            insert(account)

            // then
            assertTrue(contains(address))

            // when
            delete(address)

            // then
            assertFalse(contains(address))
        }
    }

    @Test
    fun list() = runBlocking {
        // given
        val list = (0..2).map {
            account.run {
                copy(address = address("$address$it"))
            }
        }
        repo.run {
            // when
            list.forEach { insert(it) }

            // then
            assertEquals(list, list())
        }
    }

    @Test
    fun addressList() = runBlocking {
        // given
        val addresses = (0..2).map { address("$address$it") }
        val list = addresses.map { account.copy(address = it) }

        repo.run {
            // when
            list.forEach { repo.insert(it) }

            // then
            assertEquals(addresses, repo.addressList())
        }
    }

    @Test
    fun flowList() = runBlocking {
        // given
        val addresses = (0..2).map { address("$address$it") }
        val expected = addresses.scan(emptyList<Address>()) { acc, address -> acc + address }
        val list = addresses.map { account.copy(address = it) }
        val result = async(Dispatchers.IO) { repo.flowList().take(4).toList() }

        // when
        list.forEach {
            delay(50) // makes a result more predictable and not flaky
            repo.insert(it)
        }

        // then
        assertEquals(expected, result.await())
    }

    @After
    abstract fun clearDatabase()
}

