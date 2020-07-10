package cc.cryptopunks.crypton.context

import kotlinx.coroutines.CoroutineScope

interface Connection : Net {

    data class Config(
        val scope: CoroutineScope,
        val address: Address = Address.Empty,
        val password: Password = Password.Empty
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
