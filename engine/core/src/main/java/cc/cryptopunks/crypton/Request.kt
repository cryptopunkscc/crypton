package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.logv2.Log
import cc.cryptopunks.crypton.logv2.createLog
import cc.cryptopunks.crypton.util.Singleton
import cc.cryptopunks.crypton.util.single
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.SendChannel
import java.util.*
import kotlin.coroutines.EmptyCoroutineContext

val CoroutineScope.request: Request by single()

interface RequestScope : CoroutineScope {
    val id: Long
    val action: Action
    val log: Log<Request, Any>
}

data class Request(
    override val id: Long = nextId(),
    override val action: Action = Action.Empty,
    val arg: Any = Unit,
    val root: CoroutineScope = emptyScope,
    val scope: CoroutineScope = root,
    val channels: Channels = mutableMapOf(),
    val subscriptions: Subscriptions = mutableMapOf(),
    val async: AsyncActions = WeakHashMap(),
    val handle: Handle<Action> = noHandle,
    val out: Output = {},
) : RequestScope,
    Singleton,
    CoroutineScope by scope {
    override val log = let { createLog<Request, Any> { build -> RequestLog.Event(it, build(it)) } }
    companion object
}

internal typealias Subscriptions = MutableMap<Any, Job>
internal typealias AsyncActions = WeakHashMap<Job, Any>
internal typealias Channels = MutableMap<Int, SendChannel<Request>>

private val emptyScope = CoroutineScope(EmptyCoroutineContext)
private val noHandle: Handle<Action> = { _, _ -> }
private var nextId = 0L

internal fun Request.Companion.nextId() = nextId++

