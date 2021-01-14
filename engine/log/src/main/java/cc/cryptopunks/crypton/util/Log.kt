package cc.cryptopunks.crypton.util

import cc.cryptopunks.crypton.util.logger.VoidLog

interface Log : (Log.Level, () -> Any) -> Unit {

    companion object :
        Log by BaseLogCompanion,
        LogCompanion by BaseLogCompanion

    interface Output : (Any) -> Unit

    enum class Level {
        Verbose,
        Debug,
        Info,
        Warn,
        Error
    }

    object Config {
        var level: Level = Level.Verbose
    }

    data class Event(
        val requestId: Long = 0L,
        val label: String = "no_label",
        val message: String = "",
        val throwable: Throwable? = null,
        val level: Level = Level.Debug,
        val scopes: List<String> = emptyList(),
        val action: String? = null,
        val status: String = Status.Null.name,
        val timestamp: Long = System.currentTimeMillis(),
        val thread: String = Thread.currentThread().name,
    ) {
        companion object {
            val Empty = Event()
        }

        enum class Status {
            Null,
            Sending,
            Received,
            Start,
            Handling,
            Finished,
            Failed
        }

        class Builder(
            var message: String = "",
            var throwable: Throwable? = null,
            var status: String? = null,
        )
    }
}

private object BaseLogCompanion : Log, LogCompanion {
    var output: Log.Output = VoidLog

    override fun invoke(level: Log.Level, build: () -> Any) {
        if (level >= Log.Config.level) try {
            build()
        } catch (e: Throwable) {
            Log.Event(throwable = e)
        }.run(output)
    }

    override suspend fun output(output: Log.Output) {
        this.output = output
    }
}

fun configureLog(configure: Log.Config.() -> Unit) {
    Log.Config.apply(configure)
}

