package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.Async
import cc.cryptopunks.crypton.Scoped

object Main {
    interface Action : Scoped<RootScope>

    object Command {
        data class Session(
            val status: Action
        ) : Action, Async {
            enum class Action { Reconnect, Interrupt }
        }
    }
}
