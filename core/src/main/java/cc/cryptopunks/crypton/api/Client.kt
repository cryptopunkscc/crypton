package cc.cryptopunks.crypton.api

import cc.cryptopunks.crypton.entity.*
import cc.cryptopunks.crypton.util.createDummyClass
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

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

    interface Factory : (Config) -> Client {
        data class Config(
            val resource: String = "",
            val hostAddress: String = "",
            val securityMode: SecurityMode = SecurityMode.required
        ) {
            enum class SecurityMode {
                required,
                ifpossible,
                disabled
            }
            companion object {
                val Empty = Config()
            }
        }
    }

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

    class Cache(
        private val map: MutableMap<String, Client> = mutableMapOf()
    ) :
        MutableMap<String, Client> by map,
        Flow<Client> {

        private val channel = BroadcastChannel<Client?>(Channel.CONFLATED)

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

    class Exception(
        message: String? = null,
        cause: Throwable? = null
    ) : kotlin.Exception(message, cause)

    class Empty(override val accountId: Long) : Client by DummyClient

    interface Component {
        val createClient: Factory
        val clientCache: Cache
        val mapException: MapException
    }

    class Module(
        override val createClient: Factory,
        override val clientCache: Cache = Cache(),
        override val mapException: MapException
    ) : Component

    companion object {
        private val DummyClient: Client = createDummyClass()
    }
}

typealias MapException = (Throwable) -> Throwable

val Client.isEmpty get() = this is Client.Empty