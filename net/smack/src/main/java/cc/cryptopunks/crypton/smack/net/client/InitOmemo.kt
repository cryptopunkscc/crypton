package cc.cryptopunks.crypton.smack.net.client

import cc.cryptopunks.crypton.context.Net
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import org.jivesoftware.smackx.omemo.OmemoManager

class InitOmemo(
    omemoManager: OmemoManager
) {

    private val channel = Channel<Net.Event>()

    private val init by lazy {
        try {
            omemoManager.initialize()
            channel.offer(Net.OmemoInitialized)
            true
        } catch (throwable: Throwable) {
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
