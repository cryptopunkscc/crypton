package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.smack.component.ApiComponent
import cc.cryptopunks.crypton.smack.module.ApiModule
import cc.cryptopunks.crypton.smack.module.SmackModule
import cc.cryptopunks.crypton.util.BroadcastError
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration

class SmackClient internal constructor(
    override val address: Address,
    broadcastError: BroadcastError,
    configuration: XMPPTCPConnectionConfiguration
) : ApiComponent by ApiModule(
    address = address,
    smack = SmackModule(
        configuration = configuration
    ),
    broadcastError = broadcastError
)