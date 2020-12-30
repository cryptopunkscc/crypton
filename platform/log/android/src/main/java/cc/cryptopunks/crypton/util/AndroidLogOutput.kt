package cc.cryptopunks.crypton.util

import cc.cryptopunks.crypton.util.logger.CoroutineLog
import cc.cryptopunks.crypton.util.logger.LogOutputCache
import cc.cryptopunks.crypton.util.logger.TypedLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun CoroutineScope.initAndroidLog() = launch {
    Log.output(CoroutineLog)
    TypedLog.output(CoroutineLog)
    launch { CoroutineLog.output(AndroidLogOutput) }
    launch { CoroutineLog.output(LogOutputCache.Default) }
}

private object AndroidLogOutput : Log.Output {

    private val formatLabel = columnFormatter()
    private val formatColumns = columnFormatter()

    override fun invoke(event: Any) {
        (event as? Log.Event)?.run {
            val label = listOf(label).formatLabel().first() + ":"
            val message = formatMessage().formatColumns().joinToString("")
            if (throwable != null) android.util.Log.e(
                label,
                message,
                throwable
            ) else android.util.Log.println(
                level.priority,
                label,
                message
            )
        }
    }
}

private fun Log.Event.formatMessage() = listOfNotNull(
    "| ",
    status,
    " | ",
    action?.run { "${removePackage().replace("$", ".")}" } ?: "",
    scopes.takeIf { it.isNotEmpty() }?.run { ":(${scopes.joinToString("; ")})" } ?: "",
    " | : ", message
)

private const val packageName = "cc.cryptopunks.crypton.context."
private fun String.removePackage() = replace(packageName, "")

private val Log.Level.priority
    get() = when (this) {
        Log.Level.Debug -> android.util.Log.DEBUG
        Log.Level.Error -> android.util.Log.ERROR
        Log.Level.Verbose -> android.util.Log.VERBOSE
        Log.Level.Info -> android.util.Log.INFO
        Log.Level.Warn -> android.util.Log.WARN
    }
