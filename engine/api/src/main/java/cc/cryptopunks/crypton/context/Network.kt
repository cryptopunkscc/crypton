package cc.cryptopunks.crypton.context

import kotlinx.coroutines.flow.Flow

object Network {

    sealed class Status {
        object Available : Status()
        object Changed : Status()
        object Lost : Status()
        object Unavailable: Status()

        val isConnected get() = listOf(
            Available,
            Changed
        ).any { this == it }
    }

    interface Sys {
        val getNetworkStatus: GetStatus

        interface GetStatus : Flow<Status>, () -> Status
    }
}