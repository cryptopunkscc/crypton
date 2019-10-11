package cc.cryptopunks.crypton.api

import cc.cryptopunks.crypton.entity.*
import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.ErrorHandlingScope
import kotlinx.coroutines.*

interface Api :
    Account.Api,
    User.Api,
    Presence.Api,
    Message.Api,
    Chat.Api,
    RosterEvent.Api {

    val apiScope: Scope

    class Scope(
        override val broadcast: BroadcastError
    ) : ErrorHandlingScope() {
        override val coroutineContext = SupervisorJob() + Dispatchers.IO
    }
}