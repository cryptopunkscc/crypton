package cc.cryptopunks.crypton.entity

import cc.cryptopunks.crypton.net.Net
import cc.cryptopunks.crypton.util.BroadcastErrorScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class Session(
    val address: Address,
    val scope: Scope,
    net: Net
) : Net by net {

    data class Status(
        val session: Session,
        val status: Account.Status
    )

    class Scope : BroadcastErrorScope() {
        override val coroutineContext = SupervisorJob() + Dispatchers.IO
    }

    interface StartServices: (Session) -> Unit
}