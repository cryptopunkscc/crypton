package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

private object GnomeNotification
private val log = GnomeNotification.typedLog()

suspend fun showGnomeNotification(message: Any) = coroutineScope {
    launch(Dispatchers.IO) {
        log.d("Notify $message")
        ProcessBuilder("notify-send", "'Crypton message'", "$message")
            .start()
            .apply { log.d(String(errorStream.readBytes())) }
            .waitFor()
    }
}
