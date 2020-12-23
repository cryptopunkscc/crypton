package cc.cryptopunks.crypton.util

import cc.cryptopunks.crypton.util.logger.VoidLog

interface Log : (Log.Level, () -> Log.Event) -> Unit {

    companion object : Log, LogCompanion {
        var output: Output = VoidLog

        override fun invoke(level: Level, build: () -> Event) {
            if (level >= Config.level) try {
                build()
            } catch (e: Throwable) {
                Event(throwable = e)
            }.run(output)
        }

        override suspend fun output(output: Output) {
            this.output = output
        }
    }

    interface Output : (Event) -> Unit

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
        val label: String = "no_label",
        val message: String = "",
        val throwable: Throwable? = null,
        val level: Level = Level.Debug,
        val scopes: List<String> = emptyList(),
        val action: String? = null,
        val status: String = Status.Null.name,
        val timestamp: Long = System.currentTimeMillis(),
        val thread: String = Thread.currentThread().name
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
            var status: String? = null
        ) {
            internal fun clear() {
                message = ""
                throwable = null
            }
        }
    }
}

fun configureLog(configure: Log.Config.() -> Unit) {
    Log.Config.apply(configure)
}

