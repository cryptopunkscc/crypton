package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.Connection

val setupSmackConnection: Connection.Factory.Config.() -> Unit = {
    hostAddress = "10.0.2.2"
    securityMode = Connection.Factory.Config.SecurityMode.disabled
}
