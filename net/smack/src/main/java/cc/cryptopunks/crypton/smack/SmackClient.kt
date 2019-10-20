package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.smack.component.NetComponent
import cc.cryptopunks.crypton.smack.module.NetModule
import cc.cryptopunks.crypton.smack.module.SmackModule
import cc.cryptopunks.crypton.util.BroadcastError
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration

class SmackClient internal constructor(
    override val address: Address,
    broadcastError: BroadcastError,
    configuration: XMPPTCPConnectionConfiguration
) : NetComponent by NetModule(
    address = address,
    smack = SmackModule(
        configuration = configuration
    ),
    broadcastError = broadcastError
)