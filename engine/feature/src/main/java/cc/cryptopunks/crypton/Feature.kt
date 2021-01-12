package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.cliv2.Cli
import cc.cryptopunks.crypton.util.Instance
import kotlin.coroutines.CoroutineContext

fun <A : Action> feature(
    command: Cli.Command.Template? = null,
    emitter: Emitter? = null,
    handler: Handler<A>,
):
    CoroutineContext =
    listOfNotNull<CoroutineContext>(
        handler,
        emitter,
        command?.let { CliCommand(it) }
    ).reduce { acc, coroutineContext -> acc + coroutineContext }

fun feature(
    vararg elements: Any,
):
    CoroutineContext =
    elements.map {
        when (it) {
            is CoroutineContext -> it
            is Cli.Command.Template -> CliCommand(it)
            else -> throw IllegalArgumentException()
        }
    }.reduce { acc, context -> acc + context }

data class CliCommand(
    val template: Cli.Command.Template,
) : Instance
