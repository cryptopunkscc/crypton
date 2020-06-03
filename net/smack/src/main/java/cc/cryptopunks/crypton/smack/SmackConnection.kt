package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Connection
import cc.cryptopunks.crypton.smack.module.ConnectionModule
import cc.cryptopunks.crypton.smack.module.SmackModule
import kotlinx.coroutines.CoroutineScope
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration

internal class SmackConnection(
    scope: CoroutineScope,
    address: Address,
    configuration: XMPPTCPConnectionConfiguration
) : Connection by ConnectionModule(
    scope = scope,
    address = address,
    smack = SmackModule(
        configuration = configuration,
        address = address
    )
)
