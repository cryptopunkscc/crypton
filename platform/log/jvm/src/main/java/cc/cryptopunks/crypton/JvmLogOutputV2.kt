package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.logv2.LogEvent
import cc.cryptopunks.crypton.logv2.LogOutput
import cc.cryptopunks.crypton.util.columnFormatter
import cc.cryptopunks.crypton.util.mapNotNull
import kotlinx.coroutines.CoroutineScope
import java.text.SimpleDateFormat
import java.util.*

val jvmRequestEventLogOutput: LogOutput = { logEvent ->
    (logEvent.data as? RequestLog.Event)?.let { log(logEvent, it) }
}

private fun log(
    logEvent: LogEvent,
    scope: RequestLog.Event,
) = println(formatLine(logEvent, scope))

private fun formatLine(
    logEvent: LogEvent,
    scope: RequestLog.Event,
) = logEvent
    .formatMessage(scope)
    .formatColumn()
    .joinToString(" ")

private val formatColumn = columnFormatter()

private fun LogEvent.formatMessage(event: RequestLog.Event) = listOf(
    dateFormat.format(timestamp),
    "|",
    event.id.toString(),
    "|",
    event.action.javaClass.name.formatClassName(),
    event.scopes().formatScopes(),
    "|:",
    event.data.formatData()
)

private fun List<String>.formatScopes(): String? = formatScopes(
    when {
        isEmpty() -> this
        else -> listOf("(") + this + ")"
    }
).joinToString(" ")

private val formatScopes = columnFormatter()

private val dateFormat = SimpleDateFormat("dd-MM-yy HH:mm:ss.SSS", Locale.UK)

private fun String.formatClassName(): String =
    split(".").last().replace("$", ".")

private fun CoroutineScope.scopes(): List<String> =
    coroutineContext.mapNotNull { (it as? ScopeElement)?.id }


private fun Any.formatData() = when (this) {
    is String -> this
    is RequestLog.Event.Custom -> toString()
    is RequestLog.Event.Status -> javaClass.name.formatClassName()
    is Throwable -> stackTraceToString()
    else -> toString()
}
