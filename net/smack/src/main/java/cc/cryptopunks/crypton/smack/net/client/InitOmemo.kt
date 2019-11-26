package cc.cryptopunks.crypton.smack.net.client

import cc.cryptopunks.crypton.net.Net
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.consumeAsFlow
import org.jivesoftware.smackx.omemo.OmemoManager

class InitOmemo(
    omemoManager: OmemoManager
) : Net.InitOmemo, () -> Unit, Flow<Net.Event> {

    private val channel = Channel<Net.Event>()

    private val init by lazy<Unit> {
        try {
            omemoManager.initialize()
            channel.offer(Net.Event.OmemoInitialized)
        } catch (throwable: Throwable) {
            channel.offer(
                Net.Exception(
                    message = "Omemo initialization failed",
                    cause = throwable
                )
            )
        }
    }

    override fun invoke() = init


    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<Net.Event>) {
        channel.consumeAsFlow().collect(collector)
    }
}