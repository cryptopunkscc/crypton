package cc.cryptopunks.crypton.net

import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.aSocket
import java.net.InetSocketAddress
import kotlin.coroutines.CoroutineContext

suspend fun connectClientSocket(
    address: InetSocketAddress,
    context: CoroutineContext
): Socket =
    aSocket(ActorSelectorManager(context))
        .tcp()
        .connect(address)
