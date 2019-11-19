package cc.cryptopunks.crypton.util

import timber.log.Timber
import kotlin.reflect.KClass

fun initAndroidLog() {
    Timber.plant(Timber.DebugTree())
    Log.init(AndroidLog)
}

private object AndroidLog : Log {

    override fun invoke(
        type: KClass<*>,
        level: Log.Level,
        message: Any
    ) = if (message is Throwable)
        Timber.e(message) else
        Timber.log(
            level.priority,
            prepareMessage(type, message)
        )
}

private fun prepareMessage(type: KClass<*>, message: Any) =
    "${type.java.simpleName}: $message"

private val Log.Level.priority
    get() = when (this) {
        Log.Level.Debug -> android.util.Log.DEBUG
        Log.Level.Error -> android.util.Log.ERROR
    }