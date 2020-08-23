package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.columnFormatter
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import cc.cryptopunks.crypton.util.logger.TypedLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
}

private fun Log.Event.formatMessage() = listOf(
    label,
    "|",
    status,
    ":|",
    action?.run { javaClass.name.removePackage().replace("$", ".") },
    scopes.takeIf { it.isNotEmpty() }?.run { ":(${scopes.joinToString("; ")})" },
    "|:",
    message
)

private fun String.removePackage() = replace("cc.cryptopunks.crypton.context.", "")
