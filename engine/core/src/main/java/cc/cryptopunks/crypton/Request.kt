package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.Singleton
import cc.cryptopunks.crypton.util.single
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.SendChannel
import java.util.*
import kotlin.coroutines.EmptyCoroutineContext

private var nextId = 0L
internal fun Request.Companion.nextId() = nextId++

val CoroutineScope.request: Request by single()

data class Request(
    val id: Long = -1,
    val arg: Any = Unit,
    val root: CoroutineScope = emptyScope,
    val out: Output = {},
//    val status: Status = Status.New,
    val channels: Channels = mutableMapOf(),
    val subscriptions: Subscriptions = mutableMapOf(),
    val async: AsyncActions = WeakHashMap(),
    val scope: CoroutineScope = root,
    val action: Action = Action.Empty,
    val handle: Handle<Action> = notHandle,
) : Singleton {
    enum class Status { New, Resolved, Dispatch }
    companion object
}

internal typealias Subscriptions = MutableMap<Any, Job>
internal typealias AsyncActions = WeakHashMap<Job, Any>
internal typealias Channels = MutableMap<Int, SendChannel<Request>>

private val notHandle: Handle<Action> = { _, _ -> }
private val emptyScope = CoroutineScope(EmptyCoroutineContext)
