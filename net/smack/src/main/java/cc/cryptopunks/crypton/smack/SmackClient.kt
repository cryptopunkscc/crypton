package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.net.Net
import cc.cryptopunks.crypton.smack.module.NetModule
import cc.cryptopunks.crypton.smack.module.SmackModule
import cc.cryptopunks.crypton.util.BroadcastErrorScope
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration

class SmackClient internal constructor(
    scope: BroadcastErrorScope,
    address: Address,
    configuration: XMPPTCPConnectionConfiguration
) : Net by NetModule(
    scope = scope,
    address = address,
    smack = SmackModule(
        configuration = configuration
    )
)