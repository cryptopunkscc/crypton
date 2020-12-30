package cc.cryptopunks.crypton.util.logger

import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.LogCompanion

class TypedLog(
    source: Any,
) : Log {

    internal val event = Log.Event(
        label = source.toString(),
        scopes = emptyList()
    )

    fun v(build: () -> Any) = logBuilder(Log.Level.Verbose) { build() }
    fun d(build: () -> Any) = logBuilder(Log.Level.Debug) { build() }
    fun i(build: () -> Any) = logBuilder(Log.Level.Info) { build() }
    fun w(build: () -> Any) = logBuilder(Log.Level.Warn) { build() }
    fun e(build: () -> Any) = logBuilder(Log.Level.Error) { build() }

    val builder = Builder()

    inner class Builder {
        fun v(build: Log.Event.Builder.() -> Unit) = logBuilder(Log.Level.Verbose, build)
        fun d(build: Log.Event.Builder.() -> Unit) = logBuilder(Log.Level.Debug, build)
        fun i(build: Log.Event.Builder.() -> Unit) = logBuilder(Log.Level.Info, build)
        fun w(build: Log.Event.Builder.() -> Unit) = logBuilder(Log.Level.Warn, build)
        fun e(build: Log.Event.Builder.() -> Unit) = logBuilder(Log.Level.Error, build)
    }

    override fun invoke(level: Log.Level, build: () -> Any) = TypedLogCompanion.output(build())

    companion object : LogCompanion by TypedLogCompanion
}

private object TypedLogCompanion: LogCompanion {
    var output: Log.Output = VoidLog
    override suspend fun output(output: Log.Output) {
        this.output = output
    }
}

fun Any.typedLog() = TypedLog(javaClass.simpleName)

inline fun <reified T : Any> typedLog() =
    TypedLog(T::class.java.simpleName)

private fun TypedLog.logBuilder(
    level: Log.Level,
    build: Log.Event.Builder.() -> Any,
) {
    if (level < Log.Config.level) return

    val timestamp = System.currentTimeMillis()

    invoke(level) {
        Log.Event.Builder().apply {
            when (val result = build()) {
                is Unit -> Unit
                is String -> message = result
                is Throwable -> throwable = result
                else -> message = result.toString()
            }
        }.run {
            event.copy(
                message = message,
                throwable = throwable,
                level = level,
                status = status ?: event.status,
                timestamp = timestamp
            )
        }
    }
}
