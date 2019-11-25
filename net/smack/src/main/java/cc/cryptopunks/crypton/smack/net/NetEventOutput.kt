package cc.cryptopunks.crypton.smack.net

import cc.cryptopunks.crypton.api.Api
import cc.cryptopunks.crypton.net.Net
import cc.cryptopunks.crypton.smack.util.connectionEventsFlow
import kotlinx.coroutines.flow.Flow
import org.jivesoftware.smack.tcp.XMPPTCPConnection

internal class NetEventOutput(
    connection: XMPPTCPConnection
) : Net.Event.Output, Flow<Api.Event> by connection.connectionEventsFlow()