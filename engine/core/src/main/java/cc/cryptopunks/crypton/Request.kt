package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.logv2.Log
import cc.cryptopunks.crypton.logv2.LogElement
import cc.cryptopunks.crypton.logv2.createLog
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
    override val log: LogElement<Request, Any> = requestLog(this)

    companion object
    class LogEvent(
        request: RequestScope,
        val data: Any,
    ) : RequestScope by request {
        interface Status
        object Received : Status
        object Resolved : Status
        object Start : Status
        object Finish : Status
        data class Custom(val value: String) : Status
    }
}

internal typealias Subscriptions = MutableMap<Any, Job>
internal typealias AsyncActions = WeakHashMap<Job, Any>
internal typealias Channels = MutableMap<Int, SendChannel<Request>>

private val noHandle: Handle<Action> = { _, _ -> }
private val emptyScope = CoroutineScope(EmptyCoroutineContext)

fun requestLog(
    request: Request,
): LogElement<Request, Any> = createLog<Request, Any> {
    Request.LogEvent(
        request = request,
        data = request.it()
    )
}

fun <S> Log<in Any, Any>.map(scope: S) =
    LogElement<S, Any> { level, build -> invoke(level) { scope.build() } }
