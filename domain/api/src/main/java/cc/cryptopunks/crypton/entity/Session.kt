package cc.cryptopunks.crypton.entity

import cc.cryptopunks.crypton.api.Api
import cc.cryptopunks.crypton.connection.Connection
import cc.cryptopunks.crypton.util.BroadcastErrorScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class Session(
    val address: Address,
    val scope: Scope,
    connection: Connection
) : Connection by connection {

    data class Event(
        val session: Session,
        val event: Api.Event
    )

    class Scope : BroadcastErrorScope() {
        override val coroutineContext = SupervisorJob() + Dispatchers.IO
    }
}