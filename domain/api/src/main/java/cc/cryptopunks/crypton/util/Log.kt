package cc.cryptopunks.crypton.util

import kotlin.reflect.KClass

interface Log : (KClass<*>, Log.Level, Any) -> Unit {

    enum class Level { Debug, Error }

    companion object {

        private var instance: Log? = null

        fun init(log: Log) {
            instance = log
        }

        fun print(type: KClass<*>, level: Level, message: Any) =
            instance!!.invoke(type, level, message)
    }
}


inline fun <reified T: Any> log(message: Any) =
    Log.print(
        T::class,
        Log.Level.Debug,
        message
    )

inline fun <reified T: Any> Log.Companion.d(message: Any) =
    print(
        T::class,
        Log.Level.Debug,
        message
    )

inline fun <reified T: Any> Log.Companion.e(message: Any) =
    print(
        T::class,
        Log.Level.Error,
        message
    )