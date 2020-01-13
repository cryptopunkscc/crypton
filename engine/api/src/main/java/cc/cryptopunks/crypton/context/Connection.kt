package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.util.BroadcastErrorScope

interface Connection : Net {

    data class Config(
        val scope: BroadcastErrorScope = BroadcastErrorScope(),
        val address: Address = Address.Empty,
        val password: CharSequence = ""
    ) {
        companion object {
            val Empty = Config()
        }
    }

    enum class SecurityMode {
        required,
        ifpossible,
        disabled
    }

    interface Factory : (Config) -> Connection {
        data class Config(
            val resource: String = "",
            val hostAddress: String? = null,
            val securityMode: SecurityMode = SecurityMode.ifpossible
        ) {
            companion object {
                val Empty = Config()
            }
        }
    }

    interface Core {
        val createConnection: Factory
    }
}