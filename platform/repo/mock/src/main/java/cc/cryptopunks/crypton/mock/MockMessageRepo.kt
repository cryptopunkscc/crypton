package cc.cryptopunks.crypton.mock

import androidx.paging.DataSource
import androidx.paging.listDataSource
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.util.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MockMessageRepo : Message.Repo {

    val store = Store<Map<String, Message>>(emptyMap())

    private val latest = BroadcastChannel<Message>(Channel.BUFFERED)

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
        store reduce {
            plus(message.id to message)
        }
        latest.offer(message)
    }

    override suspend fun insertOrUpdate(messages: List<Message>) {
        store reduce { plus(messages.map { it.id to it }) }
        messages.forEach {
            latest.offer(it)
        }
    }

    override suspend fun latest(): Message? =
        store.get().values.maxBy { it.timestamp }

    override suspend fun latest(chat: Address): Message? =
        store.get().values.filter { it.chat == chat }.maxBy { it.timestamp }

    override suspend fun get(id: String): Message? =
        store.get()[id]

    override suspend fun delete(id: String) {
        store.reduce { minus(id) }
    }
    override suspend fun delete(message: Message) {
        delete(message.id)
    }

    override suspend fun delete(messages: List<Message>) {
        messages.forEach { message -> delete(message) }
    }

    override suspend fun listUnread(): List<Message> =
        store.get().values.filter { it.isUnread }.sortedBy { it.timestamp }

    override suspend fun list(range: LongRange): List<Message> =
        store.get().values.toList()

    override suspend fun list(chat: Address, status: Message.Status): List<Message> {
        TODO("Not yet implemented")
    }

    override fun flowLatest(chatAddress: Address?): Flow<Message> =
        latest.asFlow().run {
            if (chatAddress == null) this
            else filter { it.chat == chatAddress }
        }

    override fun dataSourceFactory(chatAddress: Address) =
        object : DataSource.Factory<Int, Message>() {
            override fun create(): DataSource<Int, Message> = listDataSource(
                store.get()
                    .filterValues { it.chat == chatAddress }
                    .values.sortedBy { it.timestamp }
            ).also { dataSources + it }
        }

    override suspend fun listQueued(): List<Message> =
        store.get().values.filter { it.status == Message.Status.Queued }.sortedBy { it.timestamp }

    override fun flowListUnread(): Flow<List<Message>> = store.changesFlow().map { map ->
        map.filterValues { it.isUnread }.values.sortedBy { it.timestamp }
    }

    override fun flowListQueued(): Flow<List<Message>> = store.changesFlow().map { map ->
        map.filterValues { it.status == Message.Status.Queued }.values.sortedBy { it.timestamp }
    }.filter { it.isNotEmpty() }

    override fun flowUnreadCount(chatAddress: Address): Flow<Int> = store.changesFlow()
        .map { it.filterValues { it.chat == chatAddress } }
        .map { it.count { it.value.isUnread } }

    override suspend fun notifyUnread() {

    }
}
