package cc.cryptopunks.crypton.net.v2

import cc.cryptopunks.crypton.Connector
import cc.cryptopunks.crypton.net.connector
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import java.net.InetSocketAddress
import kotlin.coroutines.CoroutineContext

suspend fun clientConnector(
    socketAddress: String = "127.0.0.1",
    socketPort: Int = 2323,
    context: CoroutineContext = Dispatchers.IO
) = clientConnector(
    address = InetSocketAddress(socketAddress, socketPort),
    context = context
)

suspend fun clientConnector(
    address: InetSocketAddress,
    context: CoroutineContext = Dispatchers.IO,
): Connector = aSocket(ActorSelectorManager(context))
    .tcp()
    .connect(address)
    .connector()
