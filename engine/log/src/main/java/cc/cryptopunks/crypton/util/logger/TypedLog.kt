package cc.cryptopunks.crypton.util.logger

import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.LogCompanion

fun Any.typedLog() = TypedLog(javaClass.simpleName)

inline fun <reified T : Any> typedLog() =
    TypedLog(T::class.java.simpleName)

class TypedLog(
    source: Any
) : Log {

    private val event = Log.Event(
        label = source.toString(),
        scopes = emptyList()
    )

    fun v(build: () -> Any) = log(Log.Level.Verbose) { build() }
    fun d(build: () -> Any) = log(Log.Level.Debug) { build() }
    fun i(build: () -> Any) = log(Log.Level.Info) { build() }
    fun w(build: () -> Any) = log(Log.Level.Warn) { build() }
    fun e(build: () -> Any) = log(Log.Level.Error) { build() }

    val builder = Builder()

    inner class Builder {
        fun v(build: Log.Event.Builder.() -> Unit) = log(Log.Level.Verbose, build)
        fun d(build: Log.Event.Builder.() -> Unit) = log(Log.Level.Debug, build)
        fun i(build: Log.Event.Builder.() -> Unit) = log(Log.Level.Info, build)
        fun w(build: Log.Event.Builder.() -> Unit) = log(Log.Level.Warn, build)
        fun e(build: Log.Event.Builder.() -> Unit) = log(Log.Level.Error, build)
    }

    fun log(
        level: Log.Level,
        build: Log.Event.Builder.() -> Any
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

    override fun invoke(level: Log.Level, build: () -> Log.Event) =
        output(build())

    companion object : LogCompanion {
        var output: Log.Output = VoidLog
        override suspend fun output(output: Log.Output) {
            this.output = output
        }
    }
}
