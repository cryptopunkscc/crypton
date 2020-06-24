package cc.cryptopunks.crypton.smack.net.omemo

import cc.cryptopunks.crypton.context.Net
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import org.jivesoftware.smackx.omemo.OmemoManager

class InitOmemo(
    private val omemoManager: OmemoManager
) {
    private val log = typedLog()
    private val channel = Channel<Net.Event>()

    var isInitialized: Boolean = false
        private set


    suspend operator fun invoke() {
        if (isInitialized) return
        val jid = "${omemoManager.ownJid}/${omemoManager.deviceId}"
        try {
            log.d("start $jid")
            withContext(newSingleThreadContext(this::class.java.name)) {
                omemoManager.initialize()
            }
            channel.offer(Net.OmemoInitialized)
            log.d("stop $jid")
            isInitialized = true
        } catch (throwable: Throwable) {
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
