package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.cliv2.Cli
import cc.cryptopunks.crypton.util.CoroutineContextObject
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

fun <A : Action> feature(
    command: Cli.Command.Template? = null,
    emitter: Emitter? = null,
    handler: Handler<A>,
): CoroutineContext =
    listOfNotNull<CoroutineContext>(
        handler,
        emitter,
        command?.let { CliCommand(it) }
    ).reduce { acc, coroutineContext -> acc + coroutineContext }

fun feature(
    vararg elements: Any
) = elements.map {
    when(it) {
        is CoroutineContext -> it
        is Cli.Command.Template -> it
        else -> throw IllegalArgumentException()
    }
}

data class CliCommand(
    val template: Cli.Command.Template,
) : CoroutineContextObject

interface Feature<A : Action> : CoroutineScope {
    val emitter: Emitter? get() = null
}
