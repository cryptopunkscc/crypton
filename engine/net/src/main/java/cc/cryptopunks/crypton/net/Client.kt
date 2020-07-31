package cc.cryptopunks.crypton.net

import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.aSocket
import kotlinx.coroutines.Dispatchers
import java.net.InetSocketAddress

suspend fun Connectable.connect(
    address: InetSocketAddress
) {
    aSocket(ActorSelectorManager(Dispatchers.IO + CoroutineLog.Tag("Socket")))
        .tcp()
        .connect(address)
        .connector()
        .connect()
        .join()
}
