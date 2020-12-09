package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.dep
import kotlinx.coroutines.flow.Flow

val RootScope.networkSys: Network.Sys by dep()

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
