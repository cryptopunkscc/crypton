package cc.cryptopunks.crypton.repo

import androidx.paging.DataSource
import androidx.paging.listDataSource
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.util.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ChatRepo : Chat.Repo {

    val store = Store<Map<Address, Chat>>(emptyMap())

    private val scope = CoroutineScope(SupervisorJob() + store.dispatcher)

    private val dataSources = mutableSetOf<DataSource<Int, Chat>>()

    init {
        scope.launch {
            store.changesFlow().collect {
                dataSources.forEach { it.invalidate() }
                dataSources.clear()
            }
        }
    }

    override suspend fun get(address: Address): Chat = store.get()[address]!!

    override suspend fun list(addresses: List<Address>): List<Chat> =
        (store.get() - addresses).values.toList()

    override suspend fun insert(chat: Chat) {
        store reduce { plus(chat.address to chat) }
        dataSources.apply {
            forEach { it.invalidate() }
        }
    }

    override suspend fun insertIfNeeded(chat: Chat) {
        if (chat.address !in store.get()) insert(chat)
    }

    override suspend fun delete(chat: Chat) {
        store reduce { minus(chat.address) }
    }

    override suspend fun deleteAll() {
        store reduce { emptyMap() }
    }


    override fun dataSourceFactory(): DataSource.Factory<Int, Chat> =
        object : DataSource.Factory<Int, Chat>() {
            override fun create(): DataSource<Int, Chat> =
                listDataSource(store.get().values.toList()).also { dataSources += it }
        }

    override fun flowList(): Flow<List<Chat>> = store.changesFlow().map { it.values.toList() }
}
