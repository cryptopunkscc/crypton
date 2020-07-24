package cc.cryptopunks.crypton.util

interface Log : (String, Log.Level, Any) -> Unit {

    enum class Level { Debug, Error }

    companion object {

        private var instance: Log = Simple

        fun init(log: Log) {
            instance = log
        }

        fun print(label: String, level: Level, message: Any) {
            instance(label, level, message)
        }
    }

    object Simple : Log {
        override fun invoke(label: String, level: Level, message: Any) {
            println("$label $message")
        }
    }

    object Void : Log {
        override fun invoke(p1: String, p2: Level, p3: Any) = Unit
    }
}

inline fun <reified T: Any> Log.Companion.d(message: Any) =
    print(
        T::class.java.simpleName,
        Log.Level.Debug,
        message
    )

inline fun <reified T: Any> Log.Companion.e(message: Any) =
    print(
        T::class.java.simpleName,
        Log.Level.Error,
        message
    )
