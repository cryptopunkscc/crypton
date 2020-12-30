package cc.cryptopunks.crypton

import kotlinx.coroutines.CoroutineScope

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

    interface Async : Action {
        override val channelId: Int get() = NO_CHANNEL
    }

    interface Subscription : Action {
        override val channelId: Int get() = NO_CHANNEL
        val enable: Boolean get() = true
    }

    data class Error(
        val action: String,
        val stackTrace: String,
        val message: String?,
    ) {
        constructor(action: Any, throwable: Throwable) : this(
            action = action.javaClass.name,
            message = throwable.message,
            stackTrace = throwable.stackTraceToString()
        )
    }
}

typealias Async = Action.Async
typealias Subscription = Action.Subscription

data class Resolved(
    override val scope: CoroutineScope,
    override val action: Action,
) : Action.Resolved
