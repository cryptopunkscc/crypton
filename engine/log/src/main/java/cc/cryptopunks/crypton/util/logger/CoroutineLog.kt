package cc.cryptopunks.crypton.util.logger

import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.LogCompanion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

val CoroutineScope.log get() = CoroutineLog

fun CoroutineScope.coroutineLog() = CoroutineLog.Direct(coroutineContext)
suspend fun coroutineLog(vararg elements: CoroutineContext.Element) =
    CoroutineLog.Direct(elements.fold(coroutineContext) { acc, element -> acc + element })

object CoroutineLog : Log, LogCompanion, CoroutineScope {

    override val coroutineContext = SupervisorJob() + newSingleThreadContext(
        CoroutineLog::class.java.simpleName
    )

    private val events = BroadcastChannel<Log.Event>(Channel.BUFFERED)

    private val logger = actor<suspend () -> Log.Event>(capacity = Channel.BUFFERED) {
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

    suspend fun v(build: suspend () -> Any) = log(Log.Level.Verbose) { build() }
    suspend fun d(build: suspend () -> Any) = log(Log.Level.Debug) { build() }
    suspend fun i(build: suspend () -> Any) = log(Log.Level.Info) { build() }
    suspend fun w(build: suspend () -> Any) = log(Log.Level.Warn) { build() }
    suspend fun e(build: suspend () -> Any) = log(Log.Level.Error) { build() }

    val builder get() = Builder

    object Builder {
        suspend fun v(build: suspend Log.Event.Builder.() -> Unit) = log(Log.Level.Verbose, build)
        suspend fun d(build: suspend Log.Event.Builder.() -> Unit) = log(Log.Level.Debug, build)
        suspend fun i(build: suspend Log.Event.Builder.() -> Unit) = log(Log.Level.Info, build)
        suspend fun w(build: suspend Log.Event.Builder.() -> Unit) = log(Log.Level.Warn, build)
        suspend fun e(build: suspend Log.Event.Builder.() -> Unit) = log(Log.Level.Error, build)
    }

    suspend fun log(
        level: Log.Level,
        build: suspend Log.Event.Builder.() -> Any
    ) {
        if (level < Log.Config.level) return
        log(
            level = level,
            context = coroutineScope { coroutineContext },
            build = build
        )
    }

    internal fun log(
        level: Log.Level,
        context: CoroutineContext,
        build: suspend Log.Event.Builder.() -> Any
    ) {
        val timestamp = System.currentTimeMillis()
        val thread = Thread.currentThread().name
        logger.offer {

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
        }
    }

    override fun invoke(level: Log.Level, build: () -> Log.Event) {
        if (level < Log.Config.level) return

        logger.offer { build() }
    }

    fun flow() = events.asFlow()

    override suspend fun output(output: Log.Output) = events.asFlow().collect { output(it) }

    class Direct(val context: CoroutineContext) {
        fun v(build: suspend () -> Any) = log(Log.Level.Verbose) { build() }
        fun d(build: suspend () -> Any) = log(Log.Level.Debug) { build() }
        fun i(build: suspend () -> Any) = log(Log.Level.Info) { build() }
        fun w(build: suspend () -> Any) = log(Log.Level.Warn) { build() }
        fun e(build: suspend () -> Any) = log(Log.Level.Error) { build() }

        val builder = Build()

        inner class Build {
            fun v(build: suspend Log.Event.Builder.() -> Unit) = log(Log.Level.Verbose, build)
            fun d(build: suspend Log.Event.Builder.() -> Unit) = log(Log.Level.Debug, build)
            fun i(build: suspend Log.Event.Builder.() -> Unit) = log(Log.Level.Info, build)
            fun w(build: suspend Log.Event.Builder.() -> Unit) = log(Log.Level.Warn, build)
            fun e(build: suspend Log.Event.Builder.() -> Unit) = log(Log.Level.Error, build)
        }

        fun log(level: Log.Level, build: suspend Log.Event.Builder.() -> Any) {
            if (level < Log.Config.level) return
            log(
                level = level,
                context = context,
                build = build
            )
        }
    }

    interface Element : CoroutineContext.Element

    data class Label(val value: String) : Element {
        companion object : CoroutineContext.Key<Label>

        override val key get() = Companion
    }

    data class Scope(val id: String) : Element {
        override val key = Key(id.hashCode())

        data class Key(val hash: Int) : CoroutineContext.Key<Tag>
    }

    data class Action(val action: Any) : Element {
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


    fun Log.Event.set(context: CoroutineContext) = context.run {
        copy(
            label = get(Label)?.value ?: label,
            action = get(Action)?.action ?: action,
            status = status.takeIf { it != Log.Event.Status.Null.name }
                ?: get(Status)?.value
                ?: status,
            scopes = context.fold(emptyList()) { list, element ->
                if (element is Scope) list + element.id else list
            }
        )
    }
}
