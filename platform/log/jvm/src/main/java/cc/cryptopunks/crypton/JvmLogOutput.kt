package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.logv2.LogOutput
import cc.cryptopunks.crypton.logv2.LogScope
import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.columnFormatter
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import cc.cryptopunks.crypton.util.logger.TypedLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

fun CoroutineScope.initJvmLog() = launch {
    joinAll(
        launch { Log.output(CoroutineLog) },
        launch { TypedLog.output(CoroutineLog) },
        launch { CoroutineLog.output(JvmLogOutput) },
        LogScope.connect(
            jvmRequestEventLogOutput,
            jvmLegacyEventLogOutput
        )
    )
}

object JvmLogOutput : Log.Output {
    override fun invoke(event: Any) = when (event) {
        is Log.Event -> event.log()
        else -> Unit
    }
}

val jvmLegacyEventLogOutput: LogOutput = { logEvent ->
    (logEvent.data as? Log.Event)?.run {
        copy(
            timestamp = logEvent.timestamp,
            thread = logEvent.thread,
        ).log()
    }
}

private val formatColumn = columnFormatter()

private fun Log.Event.log() = this
    .formatMessage()
    .formatColumn()
    .joinToString(" ")
    .let(::println)
    .also { throwable?.printStackTrace() }

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
