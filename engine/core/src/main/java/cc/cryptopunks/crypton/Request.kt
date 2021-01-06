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
    val out: Output = {},
    val channels: Channels = mutableMapOf(),
    val subscriptions: Subscriptions = mutableMapOf(),
    val async: AsyncActions = WeakHashMap(),
    val scope: CoroutineScope = root,
    val handle: Handle<Action> = noHandle,
) : RequestScope,
    Singleton,
    CoroutineScope by scope {
    override val log = let { createLog<Request, Any> { build -> RequestLog.Event(it, build(it)) } }
    companion object
}

internal typealias Subscriptions = MutableMap<Any, Job>
internal typealias AsyncActions = WeakHashMap<Job, Any>
internal typealias Channels = MutableMap<Int, SendChannel<Request>>

internal fun Request.Companion.nextId() = nextId++

private var nextId = 0L

private val noHandle: Handle<Action> = { _, _ -> }

private val emptyScope = CoroutineScope(EmptyCoroutineContext)

