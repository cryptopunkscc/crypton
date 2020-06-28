package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.feature.testDirectMessaging
import cc.cryptopunks.crypton.util.Log
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    Log.init(JvmLog)
    TrustAllManager.install()
    runBlocking {
        launch {
            startServer()
        }.also {
            testDirectMessaging()
        }.cancel()
    }
}
