package cc.cryptopunks.crypton.smack.net.client

import cc.cryptopunks.crypton.context.Net
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import org.jivesoftware.smackx.omemo.OmemoManager

class InitOmemo(
    omemoManager: OmemoManager
) {
    val log = typedLog()
    private val channel = Channel<Net.Event>()

    private val init by lazy {
        try {
            log.d("start")
            omemoManager.initialize()
            channel.offer(Net.OmemoInitialized)
            log.d("stop")
            true
        } catch (throwable: Throwable) {
            log.d("failed")
            throwable.printStackTrace()
            channel.offer(
                Net.Exception(
                    message = "Omemo initialization failed",
                    cause = throwable
                )
            )
            false
        }
    }

    operator fun invoke() = init

    fun flow() = channel.consumeAsFlow()
}
