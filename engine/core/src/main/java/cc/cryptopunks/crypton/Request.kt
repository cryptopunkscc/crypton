package cc.cryptopunks.crypton

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlin.coroutines.EmptyCoroutineContext

data class Request(
    val arg: Any = Unit,
    val root: CoroutineScope = emptyScope,
    val out: Output = {},
    val status: Status = Status.New,
    val events: Channel<Any> = Channel(Channel.BUFFERED),
    val channels: Channels = mutableMapOf(),
    val subscriptions: Subscriptions = mutableMapOf(),
    override val scope: CoroutineScope = emptyScope,
    override val action: Action = Action.Empty,
    override val handle: Handle<Action> = notHandle,
) : Action.Dispatch {
    enum class Status { New, Resolved, Dispatch }
}

internal typealias Subscriptions = MutableMap<Any, Job>
internal typealias Channels = MutableMap<Int, SendChannel<Action.Dispatch>>

private val notHandle: Handle<Action> = { _, _ -> }
private val emptyScope = CoroutineScope(EmptyCoroutineContext)


internal data class Resolved(
    override val action: Action,
    override val scope: CoroutineScope,
) : Action.Resolved


internal typealias Execute = suspend Request.() -> Request

