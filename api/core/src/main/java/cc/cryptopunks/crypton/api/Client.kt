package cc.cryptopunks.crypton.api

import cc.cryptopunks.crypton.entity.*
import cc.cryptopunks.crypton.util.createDummyClass
import dagger.Provides
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

    val id: Long
    val user: User
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
        val id: Long = EmptyId,
        val remoteId: RemoteId = RemoteId.Empty,
        val password: String = ""
    ) {
        companion object {
            const val EmptyId = 0L
            val Empty = Config()
        }
    }

    @Singleton
    class Cache(
        private val map: MutableMap<Long, Client>
    ) :
        MutableMap<Long, Client> by map,
        Flow<Client> {

        private val channel = BroadcastChannel<Client?>(Channel.CONFLATED)

        @Inject
        constructor() : this(mutableMapOf())

        @InternalCoroutinesApi
        override suspend fun collect(collector: FlowCollector<Client>) {
            map.values.asFlow().collect(collector)
            channel.asFlow().filterNotNull().collect(collector)
        }

        override fun remove(key: Long) = map
            .remove(key)
            ?.apply { send(Empty(id = id)) }

        override fun put(key: Long, value: Client): Client? = map
            .put(key, value)
            .apply { send(value) }

        private fun send(client: Client) = GlobalScope.launch {
            channel.send(client)
            channel.send(null)
        }
    }

    class Empty(override val id: Long) : Client by DummyClient

    interface Component {
        val createClient: Factory
        val clientCache: Cache
    }

    @dagger.Module
    class Module(@get:Provides val client: Client) : Client {
        override val user: User @Provides get() = client.user
        override val id: Long @Provides get() = client.id
        override val create: Create @Provides get() = client.create
        override val remove: Remove @Provides get() = client.remove
        override val connect: Connect @Provides get() = client.connect
        override val disconnect: Disconnect @Provides get() = client.disconnect
        override val login: Login @Provides get() = client.login
        override val sendMessage: Message.Api.Send @Provides get() = client.sendMessage
        override val sendPresence: Presence.Api.Send @Provides get() = client.sendPresence
        override val isAuthenticated: IsAuthenticated @Provides get() = client.isAuthenticated
        override val getContacts: User.Api.GetContacts @Provides get() = client.getContacts
        override val invite: User.Api.Invite @Provides get() = client.invite
        override val invited: User.Api.Invited @Provides get() = client.invited
        override val messagePublisher: Message.Api.Publisher @Provides get() = client.messagePublisher
        override val rosterEventPublisher: RosterEvent.Api.Publisher @Provides get() = client.rosterEventPublisher
    }

    companion object {
        private val DummyClient: Client = createDummyClass()
    }
}

val Client.isEmpty get() = this is Client.Empty

fun Client.Cache.filter(user: User) = filter { it.user == user }