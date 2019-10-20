package cc.cryptopunks.crypton.api

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.manager.BaseManager
import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.CacheFlow
import cc.cryptopunks.crypton.util.ErrorHandlingScope
import cc.cryptopunks.crypton.util.createDummyClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow

interface Api {

    val apiScope: Scope
    val address: Address
    val provider: Provider

    class Scope(
        override val broadcast: BroadcastError
    ) : ErrorHandlingScope() {
        override val coroutineContext = SupervisorJob() + Dispatchers.IO
    }

    interface Provider {
        fun <T> api(): T
    }

    interface Component {
        val mapException: MapException
        val apiManager: Manager
        val currentApi: Current
    }

    interface Manager : BaseManager<Account, Api>

    class Current : CacheFlow<Api> by CacheFlow(Empty)

    interface Factory : (Config) -> Api {
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

    class Empty(override val address: Address) : Api by Dummy

    companion object {
        private val Dummy: Api = createDummyClass()
        val Empty: Api = Empty(Address.Empty)
    }
}

typealias MapException = (Throwable) -> Throwable

val Api.isEmpty get() = this is Api.Empty