package cc.cryptopunks.crypton.smack.net.omemo

import cc.cryptopunks.crypton.context.Net
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.runBlocking
import org.jivesoftware.smack.SmackException
import org.jivesoftware.smackx.omemo.OmemoManager

class InitOmemo(
    private val omemoManager: OmemoManager
) {
    private val log = typedLog()
    private val channel = Channel<Net.Event>()

    var isInitialized: Boolean = false
        private set


    operator fun invoke() = runBlocking {
        if (isInitialized) return@runBlocking
        val jid = "${omemoManager.ownJid}/${omemoManager.deviceId}"
        try {
            log.d("start $jid")
            omemoManager.initialize()
            delay(5000)
            channel.offer(Net.OmemoInitialized)
            log.d("success $jid")
            isInitialized = true
        } catch (throwable: Throwable) {
            if (throwable is SmackException.NotConnectedException) return@runBlocking
            log.d("failed $jid")
            throwable.printStackTrace()
            channel.offer(
                Net.Exception(
                    message = "Omemo initialization failed",
                    cause = throwable
                )
            )
        }
    }

    fun flow() = channel.consumeAsFlow()
}
