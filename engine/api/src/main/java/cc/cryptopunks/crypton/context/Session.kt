package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.util.BroadcastErrorScope
import cc.cryptopunks.crypton.util.OpenStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class Session(
    val address: Address,
    val scope: Scope,
    connection: Connection
) : Connection by connection {

    data class Event internal constructor(
        val session: Session,
        val event: Api.Event
    )

    @Suppress("FunctionName")
    fun Event(event: Api.Event) = Event(
        session = this,
        event = event
    )

    class Scope : BroadcastErrorScope() {
        override val coroutineContext = SupervisorJob() + Dispatchers.IO
    }

    class Store : OpenStore<Map<Address, Session>>(emptyMap())

    interface BackgroundService {
        suspend operator fun invoke()
    }
}
