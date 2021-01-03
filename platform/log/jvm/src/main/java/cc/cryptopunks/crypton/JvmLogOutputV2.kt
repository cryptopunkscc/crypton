package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.logv2.LogEvent
import cc.cryptopunks.crypton.logv2.LogOutput
import cc.cryptopunks.crypton.util.columnFormatter
import cc.cryptopunks.crypton.util.mapNotNull
import kotlinx.coroutines.CoroutineScope
import java.text.SimpleDateFormat
import java.util.*

val jvmRequestEventLogOutput: LogOutput = { logEvent ->
    (logEvent.data as? Request.LogEvent)?.let { request ->
        logEvent.log(request)
    }
}

fun Any.formatData() = when (this) {
    is String -> this
    is Request.LogEvent.Custom -> toString()
    is Request.LogEvent.Status -> javaClass.name.formatClassName()
    is Throwable -> stackTraceToString()
    else -> toString()
}


private val formatColumn = columnFormatter()

private val formatScopes = columnFormatter()

private fun LogEvent.log(scope: Request.LogEvent) = this
    .formatMessage(scope)
    .formatColumn()
    .joinToString(" ")
    .let(::println)

private fun LogEvent.formatMessage(event: Request.LogEvent) = listOf(
    dateFormat.format(timestamp),
    "|",
    event.id.toString(),
    "|",
    event.action.javaClass.name.formatClassName(),
    event.scopes().formatScopes(),
    "|:",
    event.data.formatData()
)

fun CoroutineScope.scopes(): List<String> =
    coroutineContext.mapNotNull { (it as? ScopeElement)?.id }

fun List<String>.formatScopes(): String? = formatScopes(
    when {
        isEmpty() -> this
        else -> listOf("(") + this + ")"
    }
).joinToString(" ")

private val dateFormat = SimpleDateFormat("dd-MM-yy HH:mm:ss.SSS", Locale.UK)

private fun String.formatClassName(): String =
    split(".").last().replace("$", ".")

private fun String.removePackage() = replace("cc.cryptopunks.crypton.context.", "")
