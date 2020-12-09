package cc.cryptopunks.crypton.repo.test

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.address
import cc.cryptopunks.crypton.repo.ChatRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Test

abstract class ChatRepoTest {
    abstract val repo: ChatRepo

    private val address = address("test@address")
    private val account = address("account@address")

    private val chat = Chat(
        title = "test",
        address = address,
        account = account,
    )

    @Test
    fun insertContains() = runBlocking {
        // when
        repo.insert(chat)

        // then
        Assert.assertTrue(repo.contains(address))
    }

    @Test
    fun insertGet() = runBlocking {
        // when
        repo.insert(chat)

        // then
        Assert.assertEquals(chat, repo.get(address))
    }

    @Test
    fun insertContainsDeleteContains() = runBlocking {
        // when
        repo.insert(chat)

        // then
        Assert.assertTrue(repo.contains(address))

        // when
        repo.delete(listOf(address))

        // then
        Assert.assertFalse(repo.contains(address))
    }

    @Test
    fun list() = runBlocking {
        // given
        val list = (0..2).map {
            chat.run {
                copy(address = address("$address$it"))
            }
        }

        // when
        list.forEach { repo.insert(it) }

        // then
        Assert.assertEquals(list, repo.list())
    }

    @Test
    fun flowList() = runBlocking {
        // given
        val addresses = (0..2).map { address("$address$it") }
        val list = addresses.map { chat.copy(address = it) }
        val expected = list.scan(emptyList<Chat>()) { acc, chat -> acc + chat }
        val result = async(Dispatchers.IO) { repo.flowList().take(4).toList() }

        // when
        list.forEach {
            delay(50) // makes a result more predictable and not flaky
            repo.insert(it)
        }

        // then
        Assert.assertEquals(expected, result.await())
    }

    @Test
    fun insertDeleteAllList() = runBlocking {
        // given
        val addresses = (0..5).map { address("$address$it") }
        val list = addresses.map { chat.run { copy(address = it) } }

        // when
        list.forEach { repo.insert(it) }

        // then
        Assert.assertEquals(list, repo.list())

        // when
        repo.deleteAll()

        // then
        Assert.assertEquals(emptyList<Address>(), repo.list())
    }

    @After
    abstract fun clearDatabase()
}
