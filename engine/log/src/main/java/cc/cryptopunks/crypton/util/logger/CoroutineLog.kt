package cc.cryptopunks.crypton.util.logger

import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.LogCompanion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

object CoroutineLog :
    Log,
    Log.Output,
    LogCompanion,
    CoroutineScope {

    override val coroutineContext = SupervisorJob() + newSingleThreadContext("CoroutineLog")


    override fun invoke(event: Any) = logAny(event)
    override fun invoke(level: Log.Level, build: () -> Any) = logBuilder(level, build)
    operator fun invoke(build: suspend () -> Any) = coroutineLogger.offer(build)

    fun flow() = events.asFlow()

    override suspend fun output(output: Log.Output) = events.asFlow().collect { output(it) }

    suspend fun v(build: () -> Any) = logEvent(Log.Level.Verbose) { build() }
    suspend fun d(build: () -> Any) = logEvent(Log.Level.Debug) { build() }
    suspend fun i(build: () -> Any) = logEvent(Log.Level.Info) { build() }
    suspend fun w(build: () -> Any) = logEvent(Log.Level.Warn) { build() }
    suspend fun e(build: () -> Any) = logEvent(Log.Level.Error) { build() }

    interface Element : CoroutineContext.Element

    data class Label(val value: String) : Element {
        companion object : CoroutineContext.Key<Label>

        override val key get() = Companion
    }

    data class Scope(val id: String) : Element {
        override val key = Key(id.hashCode())

        data class Key(val hash: Int) : CoroutineContext.Key<Tag>
    }

    data class Action(val action: String) : Element {
        constructor(action: Any) : this(action.javaClass.name)

        companion object : CoroutineContext.Key<Action>

        override val key get() = Companion
    }

    data class Status(val value: String) : Element {
        constructor(value: Log.Event.Status) : this(value.name)

        companion object : CoroutineContext.Key<Status>

        override val key get() = Companion
    }

    data class Tag(val any: Any) : Element {
        override val key = Key(any.hashCode())

        data class Key(val hash: Int) : CoroutineContext.Key<Tag>
    }
}


private val events = BroadcastChannel<Any>(Channel.BUFFERED)

private val coroutineLogger = CoroutineLog.actor<suspend () -> Any>(capacity = Channel.BUFFERED) {
    launch {
        channel.consumeEach { produce ->
            val event = try {
                produce()
            } catch (e: Throwable) {
                Log.Event(throwable = e)
            }
            events.offer(event)
        }
    }
}


fun Any.coroutineLogLabel() = CoroutineLog.Label(javaClass.simpleName)

private fun logAny(event: Any) {
    coroutineLogger.offer { event }
}

private fun logBuilder(level: Log.Level, build: () -> Any) {
    if (level < Log.Config.level) return

    coroutineLogger.offer { build() }
}

private val logEvent: suspend (
    level: Log.Level,
    build: Log.Event.Builder.() -> Unit,
) -> Unit = { level, build ->
    if (level >= Log.Config.level) log(
        level = level,
        context = coroutineContext,
        build = build
    )
}

internal fun log(
    level: Log.Level,
    context: CoroutineContext,
    build: Log.Event.Builder.() -> Any,
) {
    val timestamp = System.currentTimeMillis()
    val thread = Thread.currentThread().name
    coroutineLogger.offer {
        log(context, level, timestamp, thread, build)
    }
}

internal fun log(
    context: CoroutineContext,
    level: Log.Level = Log.Level.Debug,
    timestamp: Long = System.currentTimeMillis(),
    thread: String = Thread.currentThread().name,
    build: Log.Event.Builder.() -> Any,
) =
    Log.Event.Builder().apply {
    when (val result = build()) {
        is Unit -> Unit
        is String -> message = result
        is Throwable -> throwable = result
        else -> message = result.toString()
    }
}.run {
    Log.Event(
        message = message,
        throwable = throwable,
        status = status ?: Log.Event.Status.Null.name,
        level = level,
        timestamp = timestamp,
        thread = thread
    ).set(context)
}

private fun Log.Event.set(context: CoroutineContext) = context.run {
    copy(
        label = get(CoroutineLog.Label)?.value ?: label,
        action = get(CoroutineLog.Action)?.action ?: action,
        status = status.takeIf { it != Log.Event.Status.Null.name }
            ?: get(CoroutineLog.Status)?.value
            ?: status,
        scopes = context.fold(emptyList()) { list, element ->
            if (element is CoroutineLog.Scope) list + element.id else list
        }
    )
}
