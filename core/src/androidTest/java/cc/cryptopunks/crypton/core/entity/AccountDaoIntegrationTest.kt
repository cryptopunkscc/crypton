package cc.cryptopunks.crypton.core.entity

import cc.cryptopunks.crypton.core.IntegrationTest
import cc.cryptopunks.crypton.core.entity.Account.Status.*
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.Thread.sleep

class AccountDaoIntegrationTest : IntegrationTest() {


    @Test
    fun multipleInsertionsBlocking(): Unit = with(component.accountDao) {
        // given
        val ids = 1L..3
        val accounts = ids.map { account(index = it) }
        val expected = ids.map { account(index = it, withId = true) }

        // when
        accounts.forEach { insert(it) }

        // then
        assertEquals(
            expected,
            getAll()
        )
    }


    @Test
    fun multipleUpdatesBlocking(): Unit = with(component.accountDao) {
        // given
        val id = 1L
        val account = account(id).run { copy(id = insert(this)!!) }
        val statusList = listOf(
            Disconnected,
            Connecting,
            Connected
        )
        val expected = account.copy(status = Connected)

        // when
        statusList.forEach {
            update(account.copy(status = it))
        }

        // then
        assertEquals(
            expected,
            get(id)
        )
    }


    @Test
    fun observeMultipleUpdates(): Unit = with(component.accountDao) {
        // given
        val id = 1L
        val account = account(id).run { copy(id = insert(this)!!) }
        val statusList = listOf(
            Disconnected,
            Connecting,
            Connected
        )
        val test = observeChanges().test()
        val expected = statusList.map { account.copy(status = it) }

        // when
        statusList.forEach {
            update(account.copy(status = it))
            sleep(50) // sleep is required due to room doing batched transactions
        }

        // then
        test.awaitCount(statusList.count())
            .assertValueSequence(expected)
    }


    @Test
    fun observeMultipleInsertions(): Unit = with(component.accountDao) {
        // given
        val ids = 1L..3
        val accounts = ids.map { account(index = it) }
        val expected = ids.map { account(index = it, withId = true) }
        val test = observeList()
            .flatMap { Observable.fromIterable(it) }
            .distinct()
            .test()

        // when
        accounts.forEach {
            insert(it)
        }

        // then
        test.awaitCount(ids.count())
            .assertValueSequence(expected)
    }
}