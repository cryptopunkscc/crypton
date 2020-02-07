package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.util.BroadcastErrorScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class Session(
    val address: Address,
    val scope: Scope,
    connection: Connection
) : Connection by connection {

    object Created : Api.Event

    fun sessionEvent(event: Api.Event) = Event(
        session = this,
        event = event
    )

    data class Event internal constructor(
        val session: Session,
        val event: Api.Event
    )

    class Scope : BroadcastErrorScope() {
        override val coroutineContext = SupervisorJob() + Dispatchers.IO
    }

    interface Core {
        val session: Session
        val address: Address
        val sessionScope: Scope
    }
}