package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.ErrorHandlingScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object Service {
    class Scope(
        override val broadcast: BroadcastError
    ) : ErrorHandlingScope() {
        override val coroutineContext = SupervisorJob() + Dispatchers.IO
    }
}