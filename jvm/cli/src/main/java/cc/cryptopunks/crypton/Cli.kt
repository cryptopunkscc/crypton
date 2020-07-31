package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.net.connect
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.InetSocketAddress

fun main() {
//    Log.init(JvmLog) TODO
    runBlocking {
        launch { CoroutineLog.flow().collect { JvmLogOutput(it) } }
        CliClient(systemInput()).connect(InetSocketAddress("127.0.0.1", 2323))
    }
}
