package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.Log
import kotlinx.coroutines.runBlocking

fun main() {
    Log.init(JvmLog)
    runBlocking {
        CliClient().connect()
    }
}
