package cc.cryptopunks.crypton.context

import kotlinx.coroutines.CoroutineScope

interface Connection : Net {

    data class Config(
        val scope: CoroutineScope,
        val address: Address = Address.Empty,
        val password: CharSequence = ""
    )

    interface Factory : (Config) -> Connection {
        data class Config(
            val resource: String = "",
            val hostAddress: String? = null,
            val securityMode: SecurityMode = SecurityMode.ifpossible
        ) {
            companion object {
                val Empty = Config()
            }

            enum class SecurityMode {
                required,
                ifpossible,
                disabled
            }
        }
    }
}
