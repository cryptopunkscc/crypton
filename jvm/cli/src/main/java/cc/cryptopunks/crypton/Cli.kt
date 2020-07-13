package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.net.connect
import cc.cryptopunks.crypton.util.Log
import kotlinx.coroutines.runBlocking
import java.net.InetSocketAddress

fun main() {
    Log.init(JvmLog)
    runBlocking {
        CliClient(systemInput()).connect(InetSocketAddress("127.0.0.1", 2323))
    }
}
