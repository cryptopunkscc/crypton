package cc.cryptopunks.crypton.context

import kotlinx.coroutines.CoroutineScope

interface Connection : Net {

    data class Config(
        val scope: CoroutineScope,
        val account: Address = Address.Empty,
        val password: Password = Password.Empty,
        val resource: String = ""
    )

    interface Factory : (Config) -> Connection {
        data class Config(
            var resource: String = "",
            var hostAddress: String? = null,
            var securityMode: SecurityMode = SecurityMode.ifpossible
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
}
