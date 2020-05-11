package cc.cryptopunks.crypton.smack.net.api

import cc.cryptopunks.crypton.context.Api
import cc.cryptopunks.crypton.context.Net
import cc.cryptopunks.crypton.smack.net.client.InitOmemo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.jivesoftware.smack.tcp.XMPPTCPConnection

internal class NetEventBroadcast(
    private val scope: CoroutineScope,
    connection: XMPPTCPConnection,
    initOmemo: InitOmemo
) : Net.Output {

    private val channel = BroadcastChannel<Api.Event>(Channel.CONFLATED)

    init {
        scope.launch {
            flowOf(
                connection.connectionEventsFlow(),
                initOmemo
            )
                .flattenMerge()
                .collect(channel::send)
        }
    }

    fun invoke(event: Api.Event) {
        scope.launch { channel.send(event) }
    }

    override fun invoke() = channel.asFlow()
}
