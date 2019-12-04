package cc.cryptopunks.crypton.util

import timber.log.Timber

fun initAndroidLog() {
    Timber.plant(Timber.DebugTree())
    Log.init(AndroidLog)
}

private object AndroidLog : Log {

    override fun invoke(
        label: String,
        level: Log.Level,
        message: Any
    ) = if (message is Throwable)
        Timber.e(message) else
        Timber.log(
            level.priority,
            prepareMessage(label, message)
        )
}

private fun prepareMessage(label: String, message: Any) =
    "${label}: $message".replace(packageName, "")

private const val packageName = "cc.cryptopunks.crypton."

private val Log.Level.priority
    get() = when (this) {
        Log.Level.Debug -> android.util.Log.DEBUG
        Log.Level.Error -> android.util.Log.ERROR
    }