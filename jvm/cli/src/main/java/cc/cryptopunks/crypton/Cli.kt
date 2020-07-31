package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.net.connectClientSocket
import cc.cryptopunks.crypton.net.connector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.net.InetSocketAddress

fun main() {
    runBlocking {
        initJvmLog()

        CliClient(systemInput()).run {
            connectClientSocket(InetSocketAddress("127.0.0.1", 2323), Dispatchers.IO)
                .connector()
                .logging()
                .connect()
        }
    }
}
