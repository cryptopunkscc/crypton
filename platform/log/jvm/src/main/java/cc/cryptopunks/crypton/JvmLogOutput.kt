package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.columnFormatter
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import cc.cryptopunks.crypton.util.logger.TypedLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

fun CoroutineScope.initJvmLog() = launch {
    Log.output(CoroutineLog)
    TypedLog.output(CoroutineLog)
    CoroutineLog.output(JvmLogOutput)
}

object JvmLogOutput : Log.Output {

    private val formatColumn = columnFormatter()

    override fun invoke(event: Log.Event) = event
        .formatMessage()
        .formatColumn()
        .joinToString(" ")
        .let(::println)
        .also { event.throwable?.printStackTrace() }
}

private fun Log.Event.formatMessage() = listOf(
    dateFormat.format(timestamp),
    "|",
    label,
    "|",
    status,
    ":|",
    action?.run { removePackage().replace("$", ".") },
    scopes.takeIf { it.isNotEmpty() }?.run { ":(${scopes.joinToString("; ")})" },
    "|:",
    message
)

private val dateFormat = SimpleDateFormat("dd-MM-yy HH:mm:ss.SSS", Locale.UK)

private fun String.removePackage() = replace("cc.cryptopunks.crypton.context.", "")
