package cc.cryptopunks.crypton.net

import cc.cryptopunks.crypton.Connector
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.aSocket
import kotlinx.coroutines.Dispatchers
import java.net.InetSocketAddress
import kotlin.coroutines.CoroutineContext

suspend fun clientSocketConnector(
    config: Map<String, Any?> = emptyMap()
): Connector = SocketConfig(SocketConfig().default() + config).run {
    connectClientSocket(
        InetSocketAddress(socketAddress, socketPort),
        Dispatchers.IO
    ).connector()
}

suspend fun connectClientSocket(
    address: InetSocketAddress,
    context: CoroutineContext
): Socket =
    aSocket(ActorSelectorManager(context))
        .tcp()
        .connect(address)

class SocketConfig(
    map: Map<String, Any?> = emptyMap()
) : MutableMap<String, Any?> by map.toMutableMap() {
    var socketAddress: String by this
    var socketPort: Int by this
}

fun SocketConfig.default() = apply {
    socketAddress = "127.0.0.1"
    socketPort = 2323
}
