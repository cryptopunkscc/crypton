package cc.cryptopunks.crypton.smack.net.omemo

import cc.cryptopunks.crypton.context.Net
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import org.jivesoftware.smack.SmackException
import org.jivesoftware.smackx.omemo.OmemoManager

class InitOmemo(
    private val omemoManager: OmemoManager
) {
    private val log = typedLog()
    private val channel = Channel<Net.Event>()
    private val context = newSingleThreadContext("Omemo ${omemoManager.ownJid}")
    var isInitialized: Boolean = false
        private set


    suspend operator fun invoke() {
        if (isInitialized) return
        val jid = "${omemoManager.ownJid}/${omemoManager.deviceId}"
        try {
            log.d("start $jid")
            withContext(context) {
                omemoManager.initialize()
            }
            delay(3000)
            isInitialized = true
            log.d("success $jid")
            channel.offer(Net.OmemoInitialized)
        } catch (throwable: Throwable) {
            if (throwable is SmackException.NotConnectedException) return
            log.d("failed $jid")
            throwable.printStackTrace()
            channel.send(
                Net.Exception(
                    message = "Omemo initialization failed",
                    cause = throwable
                )
            )
        }
    }

    fun flow() = channel.consumeAsFlow()
}
