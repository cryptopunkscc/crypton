package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.cliv2.Cli
import cc.cryptopunks.crypton.cliv2.prepare
import cc.cryptopunks.crypton.cliv2.reduce
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.core.cliv2.cryptonCommands
import cc.cryptopunks.crypton.format.format
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import java.util.*

class CliClient(
    private val cliInput: Flow<String>
) : Connectable {

    override val coroutineContext = SupervisorJob() +
        Dispatchers.IO +
        CoroutineLog.Label(javaClass.simpleName)

    override fun Connector.connect(): Job = launch {
        val inputJob = launch {
            input
                .mapNotNull(format)
                .collect(::println)
        }
        launch {
            cliInput
                .scan(Cli.Context(cryptonCommands), reduce)
                .mapNotNull(getResults)
                .mapNotNull(format)
                .collect(::println)
            delay(1000)
            inputJob.cancel()
        }
    }
}

private val reduce: suspend Cli.Context.(String) -> Cli.Context
    get() = { input ->
        when (result) {
            is Cli.Result.Return,
            is Cli.Result.Error -> prepare()
            else -> this
        }.reduce(Cli.Input.Raw(input))
    }

private val Connector.getResults: suspend Cli.Context.() -> Any?
    get() = {
        when (val result = result) {
            is Cli.Result.Return -> when (result.value) {
                is Cli.Config -> result.value
                else -> result.value.out()
            }
            is Cli.Result.Suggestion -> result.value
            is Cli.Result.Error -> result.throwable.printStackTrace()
            else -> null
        }
    }

private val format: suspend (Any) -> String?
    get() = { arg ->
        when (arg) {
            is Cli.Execute -> arg.format()
            is Roster.Items -> arg.format()
            is Unit -> null
            else -> arg.toString()
        }
    }

private fun Roster.Items.format() = list.map { item ->
    item.run { "${Date(updatedAt)} ($unreadMessagesCount) $account - $chatAddress: ${message.text}" }
}.joinToString("\n")
