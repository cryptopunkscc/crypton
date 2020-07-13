package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.typedLog
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.aSocket
import kotlinx.coroutines.Dispatchers
import java.net.InetSocketAddress

suspend fun Connectable.connect(
    address: InetSocketAddress
) {
    val log = typedLog()
    aSocket(ActorSelectorManager(Dispatchers.IO)).tcp()
        .connect(address)
        .connector(log)
        .connect()
        .join()
}
