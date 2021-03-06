package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.cliv2.Cli
import cc.cryptopunks.crypton.cliv2.reduce
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.format.format
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun cliClient(
    console: Connector,
    backend: Connector,
    context: Cli.Context
): suspend () -> Unit = {
    withContext(
        SupervisorJob() + Dispatchers.IO + CoroutineLog.Label("CliClient")
    ) {
        val inputJob = launch {
            backend.input
                .map { any ->
                    any.formatCliOutput() ?: any.toString()
                }
                .collect(console.output)
        }
        console.input
            .filterIsInstance<String>()
            .scan(context) { context, input ->
                context.reduce(input)
            }
            .map { context ->
                context.unwrapCliResult()
            }
            .collect { result ->
                when (result) {
                    is Action -> backend.output(result)
                    else -> result.formatCliOutput()?.let { console.output(it) }
                }
            }
        delay(1000) // TODO ultimately, coroutine should wait for expected result
        inputJob.cancel()
    }
}

fun Any.unwrapCliResult(): Any =
    when (this) {
        is Cli.Context -> result
        is Cli.Result.Return -> value
        is Cli.Result.Suggestion -> value
        is Cli.Result.Error -> value
        else -> null
    }
        ?.unwrapCliResult()
        ?: this

fun Any.formatCliOutput(): String? =
    when (this) {
        is Cli.Config -> map.toString()
        is Cli.Execute -> format()
        is Cli.Param -> format()
        is Cli.Params -> format()
        is Roster.Items -> format()
        is Roster.Item -> format()
        is Chat.Messages -> format()
        is Message -> format()
        is Map<*, *> -> toMap().toString()
        is Throwable -> stackTraceToString()
        else -> null
    }
