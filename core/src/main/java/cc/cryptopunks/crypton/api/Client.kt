package cc.cryptopunks.crypton.api

import cc.cryptopunks.crypton.entity.*
import cc.cryptopunks.crypton.manager.BaseManager
import cc.cryptopunks.crypton.util.CacheFlow
import cc.cryptopunks.crypton.util.createDummyClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface Client :
    Api,
    Account.Api,
    User.Api,
    Presence.Api,
    Message.Api,
    Chat.Api,
    RosterEvent.Api,
    CoroutineScope {

    override val coroutineContext
        get() = apiScope.coroutineContext

    interface Connect : Account.Api.Connect
    interface Disconnect : Account.Api.Disconnect
    interface IsConnected : Account.Api.IsConnected
    interface InitOmemo : Account.Api.InitOmemo

    interface Component {
        val mapException: MapException
        val clientManager: Manager
        val currentClient: Current
    }

    interface Manager : BaseManager<Account, Client>

    class Current : CacheFlow<Client> by CacheFlow(Empty)

    interface Factory : (Config) -> Client {
        data class Config(
            val resource: String = "",
            val hostAddress: String? = null,
            val securityMode: SecurityMode = SecurityMode.ifpossible
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
        val address: Address = Address.Empty,
        val password: String = ""
    ) {
        companion object {
            val Empty = Config()
        }
    }

    class Exception(
        message: String? = null,
        cause: Throwable? = null
    ) : kotlin.Exception(message, cause) {

        interface Output : Flow<Exception>
    }

    class Empty(override val address: Address) : Client by Dummy

    companion object {
        private val Dummy: Client = createDummyClass()
        val Empty: Client = Empty(Address.Empty)
    }
}

typealias MapException = (Throwable) -> Throwable

val Client.isEmpty get() = this is Client.Empty