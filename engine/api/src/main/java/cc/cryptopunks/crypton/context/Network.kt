package cc.cryptopunks.crypton.context

import kotlinx.coroutines.flow.Flow

object Network {

    sealed class Status {
        object Available : Status()
        object Changed : Status()
        object Lost : Status()
        object Unavailable: Status()
    }

    interface Sys {
        interface GetStatus : Flow<Status>, () -> Status
    }
}