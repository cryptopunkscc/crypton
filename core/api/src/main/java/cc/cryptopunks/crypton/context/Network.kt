package cc.cryptopunks.crypton.context

import kotlinx.coroutines.flow.Flow

object Network {

    data class Adapter(
        val network: String?,
        val status: Status
    )

    sealed class Status : Main.Action {
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
        val status: Status
        fun statusFlow() : Flow<Status>
    }
}
