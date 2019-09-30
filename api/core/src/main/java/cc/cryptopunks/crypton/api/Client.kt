package cc.cryptopunks.crypton.api

import cc.cryptopunks.crypton.entity.*
import cc.cryptopunks.crypton.util.createDummyClass
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

interface Client:
    User.Api,
    Presence.Api,
    Message.Api,
    RosterEvent.Api {

    val accountId: Long
    val address: Address
    val create: Create
    val remove: Remove
    val login: Login
    val connect: Connect
    val disconnect: Disconnect
    val isAuthenticated: IsAuthenticated

    interface Create: () -> Unit
    interface Remove: () -> Unit
    interface Login: () -> Unit
    interface Connect: () -> Unit
    interface Disconnect: () -> Unit
    interface IsAuthenticated: () -> Boolean

    interface Factory : (Config) -> Client

    data class Config(
        val accountId: Long = EmptyId,
        val address: Address = Address.Empty,
        val password: String = ""
    ) {
        companion object {
            const val EmptyId = 0L
            val Empty = Config()
        }
    }

    @Singleton
    class Cache(
        private val map: MutableMap<String, Client>
    ) :
        MutableMap<String, Client> by map,
        Flow<Client> {

        private val channel = BroadcastChannel<Client?>(Channel.CONFLATED)

        @Inject
        constructor() : this(mutableMapOf())

        @InternalCoroutinesApi
        override suspend fun collect(collector: FlowCollector<Client>) {
            map.values.asFlow().collect(collector)
            channel.asFlow().filterNotNull().collect(collector)
        }

        override fun remove(key: String) = map
            .remove(key)
            ?.apply { send(Empty(accountId = accountId)) }

        override fun put(key: String, value: Client): Client? = map
            .put(key, value)
            .apply { send(value) }

        private fun send(client: Client) = GlobalScope.launch {
            channel.send(client)
            channel.send(null)
        }
    }

    class Empty(override val accountId: Long) : Client by DummyClient

    interface Component {
        val createClient: Factory
        val clientCache: Cache
    }

    companion object {
        private val DummyClient: Client = createDummyClass()
    }
}

val Client.isEmpty get() = this is Client.Empty