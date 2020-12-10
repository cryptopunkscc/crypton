package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.dep
import kotlinx.coroutines.CoroutineScope

val RootScope.createConnection: Connection.Factory by dep()

interface Connection {

    val net: Net
    val accountNet: Account.Net
    val messageNet: Message.Net
    val chatNet: Chat.Net
    val rosterNet: Roster.Net
    val deviceNet: Device.Net
    val uploadNet: Upload.Net

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
