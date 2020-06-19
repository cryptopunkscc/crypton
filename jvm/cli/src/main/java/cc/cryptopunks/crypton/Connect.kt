package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.typedLog
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.aSocket
import kotlinx.coroutines.Dispatchers
import java.net.InetSocketAddress

suspend fun CliClient.connect(
    host: String = "127.0.0.1",
    port: Int = 2323
) {
    val log = typedLog()
    aSocket(ActorSelectorManager(Dispatchers.IO)).tcp()
        .connect(InetSocketAddress(host, port))
        .connector(log)
        .connect()
        .join()
}
