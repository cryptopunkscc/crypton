package cc.cryptopunks.crypton.repo

import androidx.paging.DataSource
import androidx.paging.listDataSource
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.util.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MessageRepo : Message.Repo {

    private val store = Store<Map<String, Message>>(emptyMap())

    private val scope = CoroutineScope(SupervisorJob() + store.dispatcher)

    private val dataSources = mutableSetOf<DataSource<Int, Message>>()

    init {
        scope.launch {
            store.changesFlow().collect {
                dataSources.forEach { it.invalidate() }
                dataSources.clear()
            }
        }
    }

    override suspend fun insertOrUpdate(message: Message) {
        store reduce { plus(message.id to message) }
    }

    override suspend fun insertOrUpdate(messages: List<Message>) {
        store reduce { plus(messages.map { it.id to it }) }
    }

    override suspend fun latest(): Message? =
        store.get().values.maxBy { it.timestamp }

    override suspend fun get(id: String): Message? =
        store.get()[id]

    override suspend fun delete(message: Message) {
        store.reduce { minus(message.id) }
    }

    override suspend fun listUnread(): List<Message> =
        store.get().values.filter { it.isUnread }.sortedBy { it.timestamp }

    override fun flowLatest(chat: Chat): Flow<Message> {
        var last = emptySet<String>()
        return store.changesFlow().mapNotNull { current ->
            (current - last)
                .also { last = current.keys }
                .values.maxBy { it.timestamp }
        }
    }

    override fun dataSourceFactory(chat: Chat) = object : DataSource.Factory<Int, Message>() {
        override fun create(): DataSource<Int, Message> = listDataSource(
            store.get()
                .filterValues { it.chatAddress == chat.address }
                .values.sortedBy { it.timestamp }
        ).also { dataSources + it }
    }

    override fun unreadListFlow(): Flow<List<Message>> = store.changesFlow()
        .map { it.filterValues { it.isUnread }.values.sortedBy { it.timestamp } }

    override fun unreadCountFlow(chat: Chat): Flow<Int> = store.changesFlow()
        .map { it.filterValues { it.chatAddress == chat.address } }
        .map { it.count { it.value.isUnread } }

    override suspend fun notifyUnread() {

    }

}
