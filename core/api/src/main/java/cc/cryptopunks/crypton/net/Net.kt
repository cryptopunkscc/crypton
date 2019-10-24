package cc.cryptopunks.crypton.net

import cc.cryptopunks.crypton.entity.*
import cc.cryptopunks.crypton.util.BroadcastErrorScope
import kotlinx.coroutines.flow.Flow

interface Net:
    Account.Net,
    User.Net,
    Presence.Net,
    Message.Net,
    Chat.Net,
    RosterEvent.Net {

    interface Component {
        val createNet: Factory
        val mapException: MapException
    }

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
        val scope: BroadcastErrorScope = BroadcastErrorScope(),
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
}

typealias MapException = (Throwable) -> Throwable

