package cc.cryptopunks.crypton.net

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

interface Net {

    val apiScope: Scope
    val address: Address
    val provider: Provider

    class Scope(
        override val broadcast: BroadcastError
    ) : ErrorHandlingScope() {
        override val coroutineContext = SupervisorJob() + Dispatchers.IO
    }

    interface Provider {
        fun <T> net(): T
    }

    interface Component {
        val mapException: MapException
        val netManager: Manager
        val currentNet: Current
    }

    interface Manager : BaseManager<Account, Net>

    class Current : CacheFlow<Net> by CacheFlow(Empty)

    interface Factory : (Config) -> Net {
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

    class Empty(override val address: Address) : Net by Dummy

    companion object {
        private val Dummy: Net = createDummyClass()
        val Empty: Net = Empty(Address.Empty)
    }
}

typealias MapException = (Throwable) -> Throwable

val Net.isEmpty get() = this is Net.Empty