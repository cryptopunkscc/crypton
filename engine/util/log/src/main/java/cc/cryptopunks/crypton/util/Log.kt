package cc.cryptopunks.crypton.util

interface Log : (String, Log.Level, Any) -> Unit {

    enum class Level { Debug, Error }

    companion object {

        private var instance: Log? = null

        fun init(log: Log) {
            instance = log
        }

        fun print(label: String, level: Level, message: Any) =
            instance!!.invoke(label, level, message)
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