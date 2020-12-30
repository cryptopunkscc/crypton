package cc.cryptopunks.crypton.factory

import cc.cryptopunks.crypton.Connector
import cc.cryptopunks.crypton.Service
import kotlinx.coroutines.flow.asFlow

fun Service.connector() = Connector(
    input = output.asFlow(),
    output = { input.send(this) }
)
