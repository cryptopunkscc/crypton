package cc.cryptopunks.crypton

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.EmptyCoroutineContext

const val DEFAULT_CHANNEL = 0
const val NO_CHANNEL = -1

internal val Action.type: Any get() = javaClass

interface Action {
    val channelId: Int get() = DEFAULT_CHANNEL

    object Empty : Action

    interface Resolved : Action {
        val action: Action
        val scope: CoroutineScope
    }

    interface Dispatch : Resolved {
        val handle: Handle<Action>
    }

    data class Error(
        val action: Action,
        val throwable: Throwable,
    )
}

interface Async : Action {
    override val channelId: Int get() = NO_CHANNEL
}

interface Subscription : Action {
    override val channelId: Int get() = NO_CHANNEL
    val enable: Boolean get() = true
}

data class Scoped(
    val scope: String,
    val action: Action,
) : Action
