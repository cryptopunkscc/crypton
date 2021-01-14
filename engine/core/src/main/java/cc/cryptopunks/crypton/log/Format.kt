package cc.cryptopunks.crypton.log

import cc.cryptopunks.crypton.RequestLog
import cc.cryptopunks.crypton.util.columnFormatter
import java.text.SimpleDateFormat
import java.util.*

internal fun RequestLog.Data.formatMessage() = listOf(
    dateFormat.format(timestamp),
    eventId.toString(),
    time.toString(),
    actionType.name.formatClassName(),
    scopes.formatScopes(),
    data.formatData()
).formatColumn()

private val dateFormat = SimpleDateFormat("dd-MM-yy HH:mm:ss.SSS", Locale.UK)

private fun String.formatClassName(): String =
    split(".").last().replace("$", ".")

private fun List<String>.formatScopes(): String? = formatScopes(
    when {
        isEmpty() -> this
        else -> listOf("(") + this + ")"
    }
).joinToString(" ")

private val formatScopes = columnFormatter()

private fun Any.formatData() = when (this) {
    is String -> this
    is RequestLog.Event.Custom -> toString()
    is RequestLog.Event.Status -> javaClass.name.formatClassName()
    is Throwable -> stackTraceToString()
    else -> toString()
}

private val formatColumn = columnFormatter()
