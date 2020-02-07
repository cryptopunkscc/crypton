package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Connection
import cc.cryptopunks.crypton.smack.module.ConnectionModule
import cc.cryptopunks.crypton.smack.module.SmackModule
import cc.cryptopunks.crypton.util.BroadcastErrorScope
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration

class SmackConnection internal constructor(
    scope: BroadcastErrorScope,
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