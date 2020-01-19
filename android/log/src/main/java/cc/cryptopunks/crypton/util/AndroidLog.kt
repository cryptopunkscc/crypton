package cc.cryptopunks.crypton.util

fun initAndroidLog() {
    Log.init(AndroidLog)
}

private object AndroidLog : Log {

    override fun invoke(
        label: String,
        level: Log.Level,
        message: Any
    ) {
        if (message is Throwable)
            android.util.Log.e(null, "", message) else
            android.util.Log.println(
                level.priority,
                label.split(".").last(),
                message.toString()
            )
    }
}

private fun prepareMessage(label: String, message: Any) =
    "${label}: $message".replace(packageName, "")

private const val packageName = "cc.cryptopunks.crypton."

private val Log.Level.priority
    get() = when (this) {
        Log.Level.Debug -> android.util.Log.DEBUG
        Log.Level.Error -> android.util.Log.ERROR
    }