package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.net.connect
import cc.cryptopunks.crypton.net.startServerSocket
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress


fun server(
    config: Map<String, Any?>,
    backend: Connectable
): suspend  () -> Unit = {
    withContext(
        CoroutineLog.Label("CryptonServer")
    ) {
        ServerConfig(config).run {
            startServerSocket(
                InetSocketAddress(
                    socketAddress,
                    socketPort
                )
            ).connect(backend)
        }
    }
}

internal class ServerConfig(
    map: Map<String, Any?> = emptyMap()
) : MutableMap<String, Any?> by map.toMutableMap() {
    var home: String by this
    var omemoStore: String by this
    var name: String by this
    var socketAddress: String by this
    var socketPort: Int by this
    var hostAddress: String? by this
    var securityMode: String by this
    var inMemory: String by this
}


internal fun ServerConfig.default() = apply {
    home = "~/.crypton".replaceFirst("~", System.getProperty("user.home"))
    omemoStore = "omemo_store"
    socketPort = 2323
    socketAddress = "127.0.0.1"
    hostAddress = null
    securityMode = "ifPossible"
    inMemory = "false"
}

internal fun ServerConfig.local() = apply {
    hostAddress = "127.0.0.1"
    securityMode = "disabled"
}
