package cc.cryptopunks.crypton.api

import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.ErrorHandlingScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

interface Api {

    val apiScope: Scope
    val address: Address

    class Scope(
        override val broadcast: BroadcastError
    ) : ErrorHandlingScope() {
        override val coroutineContext = SupervisorJob() + Dispatchers.IO
    }

    interface Provider {
        fun <T> api(): T
    }
}