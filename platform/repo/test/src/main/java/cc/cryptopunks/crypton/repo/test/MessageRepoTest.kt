package cc.cryptopunks.crypton.repo.test

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Repo
import cc.cryptopunks.crypton.context.Resource
import cc.cryptopunks.crypton.context.address
import cc.cryptopunks.crypton.repo.ChatRepo
import cc.cryptopunks.crypton.repo.MessageRepo
import cc.cryptopunks.crypton.util.CreatePagedList
import cc.cryptopunks.crypton.util.asFlow
import cc.cryptopunks.crypton.util.pagedListConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

abstract class MessageRepoTest {
    abstract val repo: MessageRepo
    abstract val chatRepo: ChatRepo

    private val user1 = address("user1@address")
    private val user2 = address("user2@address")
    private val id = "id"

    private val timestamp = System.currentTimeMillis()

    private val message = Message(
        id = id,
        stanzaId = id,
        body = "text",
        timestamp = timestamp,
        chat = user2,
        from = Resource(user1),
        to = Resource(user2),
        status = Message.Status.Sending,
        readAt = timestamp,
        encrypted = true
    )

    @Test
    fun insertOrUpdateGetDelete() = runBlocking {
        repo.run {
            // when
            insertOrUpdate(message)

            // then
            assertEquals(message, get(id))

            // when
            val updated = message.copy(status = Message.Status.Sent)

            insertOrUpdate(updated)

            // then
            assertEquals(updated, get(id))

            // when
            delete(updated)

            // then
            assertNull(get(id))
        }
    }

    @Test
    fun insertOrUpdateGetDeleteById() = runBlocking {
        repo.run {
            // when
            insertOrUpdate(message)

            // then
            assertNotNull(get(id))

            // when
            delete(id)

            // then
            assertNull(get(id))
        }
    }

    @Test
    fun insertOrUpdateLatest() = runBlocking {
        repo.run {
            // given
            val list = (0..5).map {
                message.copy(
                    id = "$it",
                    timestamp = timestamp + it
                )
            }

            // when
            insertOrUpdate(list)

            // then
            assertEquals(list.last(), latest())
        }
    }

    @Test
    fun insertOrUpdateLatestByChat() = runBlocking {
        repo.run {
            // given
            val list = (0..5).map {
                message.copy(
                    id = "$it",
                    timestamp = timestamp + it,
                    chat = if (it % 2 == 0) user1 else user2
                )
            }

            // when
            insertOrUpdate(list)

            // then
            assertEquals(
                list[list.size - 2],
                latest(user1)
            )
        }
    }

    @Test
    fun insertOrUpdateListDelete() = runBlocking {
        repo.run {
            // given
            val list = (0..5).map { message.copy(id = "$it") }

            // when
            insertOrUpdate(list)

            // then
            assertEquals(list, list())

            // when
            val updated = list.mapIndexed { index, message ->
                if (index < 2) message
                else message.copy(status = Message.Status.Sent)
            }

            insertOrUpdate(updated)

            // then
            assertEquals(updated, list())

            // when
            delete(updated)

            // then
            assertEquals(emptyList<Message>(), list())
        }
    }

    @Test
    fun insertOrUpdateListRange() = runBlocking {
        repo.run {
            // given
            val list = (0..5).map {
                message.copy(
                    id = "$it",
                    timestamp = timestamp + it
                )
            }

            // when
            insertOrUpdate(list)

            // then
            assertEquals(
                list.take(3),
                list(range = (timestamp)..(timestamp + 2))
            )
        }
    }

    @Test
    fun insertOrUpdateListByChatAndStatus() = runBlocking {
        repo.run {
            // given
            val list = (0..10).map {
                message.copy(
                    id = "$it",
                    timestamp = timestamp + it,
                    chat = if (it % 2 == 0) user1 else user2,
                    status = if (it % 3 == 0) Message.Status.Sent else Message.Status.Sending
                )
            }

            // when
            insertOrUpdate(list)

            // then
            assertEquals(
                listOf(list[0], list[6]),
                list(user1, Message.Status.Sent)
            )
        }
    }

    @Test
    fun insertOrUpdateListUnread() = runBlocking {
        repo.run {
            // given
            val list = (0..5).map {
                message.copy(
                    id = "$it",
                    timestamp = timestamp + it,
                    readAt = 0,
                    status = if (it % 2 == 0)
                        Message.Status.Sent else
                        Message.Status.Received
                )
            }

            // when
            insertOrUpdate(list)

            // then
            assertEquals(
                list.filter { it.status == Message.Status.Received && it.readAt == 0L },
                listUnread()
            )
        }
    }

    @Test
    fun insertOrUpdateListQueued() = runBlocking {
        repo.run {
            // given
            val list = (0..5).map {
                message.copy(
                    id = "$it",
                    timestamp = timestamp + it,
                    readAt = 0,
                    status = if (it % 2 == 0)
                        Message.Status.Sent else
                        Message.Status.Queued
                )
            }

            // when
            insertOrUpdate(list)

            // then
            assertEquals(
                list.filter { it.status == Message.Status.Queued },
                listQueued()
            )
        }
    }

    @Test
    fun dataSourceFactory() = runBlocking {
        repo.run {
            // given
            val list = (0..5).map {
                message.copy(
                    id = "$it",
                    timestamp = timestamp + it
                )
            }

            val result = async {
                CreatePagedList(
                    config = pagedListConfig(pageSize = 3),
                    dataSourceFactory = dataSourceFactory(user2),
                    fetchExecutor = Repo.Context.Query().executor,
                    notifyExecutor = Repo.Context.Transaction().executor
                ).asFlow().map { pagedList ->
                    pagedList.toList()
                }.take(7).toList()
            }

            // when
            list.forEach {
                delay(100) // makes a result more predictable and not flaky
                insertOrUpdate(it)
            }

            // then
            assertEquals(
                list.scan(emptyList<Message>()) { acc, message -> listOf(message) + acc },
                result.await()
            )
        }
    }

    @Test
    fun insertOrUpdateFlowLatest() = runBlocking {
        repo.run {
            // given
            val list = (0..2).map {
                message.copy(
                    id = "$it",
                    timestamp = timestamp + it
                )
            }
            val result = async(Dispatchers.IO) { flowLatest().take(3).toList() }

            // when
            list.forEach {
                delay(50) // makes a result more predictable and not flaky
                insertOrUpdate(it)
            }
            // then
            assertEquals(list, result.await())
        }
    }

    @Test
    fun insertOrUpdateFlowLatestByChat() = runBlocking {
        repo.run {
            // given
            val list = (0..5).map {
                message.copy(
                    id = "$it",
                    body = "text$it",
                    chat = if (it % 2 == 0) user1 else user2,
                    timestamp = timestamp + it
                )
            }

            val result = async(Dispatchers.IO) { flowLatest(user1).take(3).toList() }

            // when
            list.forEach {
                delay(50) // makes a result more predictable and not flaky
                insertOrUpdate(it)
            }

            // then
            assertEquals(
                list.filter { it.chat == user1 },
                result.await()
            )
        }
    }


    @Test
    fun insertOrUpdateFlowListQueued() = runBlocking {
        repo.run {
            // given
            val list = (0..5).map {
                message.copy(
                    id = "$it",
                    timestamp = timestamp + it,
                    readAt = 0,
                    status = if (it % 2 == 0)
                        Message.Status.Sent else
                        Message.Status.Queued
                )
            }
            val result = async(Dispatchers.IO) { flowListQueued().take(3).toList() }

            // when
            list.forEach {
                delay(50) // makes a result more predictable and not flaky
                insertOrUpdate(it)
            }
            // then
            assertEquals(
                list.filter { it.status == Message.Status.Queued }
                    .scan(emptyList<Message>()) { acc, message -> acc + message }
                    .drop(1),
                result.await()
            )
        }
    }

    @Test
    fun insertOrUpdateFlowListUnread() = runBlocking {
        repo.run {
            // given
            val list = (0..5).map {
                message.copy(
                    id = "$it",
                    timestamp = timestamp + it,
                    readAt = 0,
                    status = if (it % 2 == 0)
                        Message.Status.Sent else
                        Message.Status.Received
                )
            }
            val result = async(Dispatchers.IO) { flowListUnread().take(4).toList() }

            // when
            list.forEach {
                delay(50) // makes a result more predictable and not flaky
                insertOrUpdate(it)
                notifyUnread()
            }
            // then
            assertEquals(
                list.filter { it.status == Message.Status.Received && it.readAt == 0L }
                    .scan(emptyList<Message>()) { acc, message -> acc + message },
                result.await()
            )
        }
    }

    @Test
    fun insertOrUpdateFlowUnreadCount() = runBlocking {
        repo.run {
            // given
            val list = (0..5).map {
                message.copy(
                    id = "$it",
                    timestamp = timestamp + it,
                    readAt = 0,
                    status = if (it % 2 == 0)
                        Message.Status.Sent else
                        Message.Status.Received,
                    chat = if (it % 3 == 0) user1 else user2,
                )
            }
            val result = async { flowUnreadCount(user2).take(3).toList() }

            // when
            delay(50) // makes a result more predictable and not flaky
            notifyUnread()
            list.forEach {
                delay(50) // makes a result more predictable and not flaky
                insertOrUpdate(it)
                notifyUnread()
            }

            // then
            assertEquals(
                listOf(0, 1, 2),
                result.await()
            )
        }
    }

    @Before
    fun prepareChat() {
        runBlocking {
            chatRepo.run {
                insert(Chat(address = user1))
                insert(Chat(address = user2))
            }
        }
    }

    @After
    abstract fun clearDatabase()
}
