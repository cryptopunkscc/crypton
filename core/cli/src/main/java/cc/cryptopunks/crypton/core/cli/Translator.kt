package cc.cryptopunks.crypton.core.cli

import cc.cryptopunks.crypton.cli.CliContext
import cc.cryptopunks.crypton.cli.prepare
import cc.cryptopunks.crypton.cli.process
import cc.cryptopunks.crypton.context.Exec
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan

fun Flow<CharSequence>.translateMessageInput(
    context: CliContext = context().prepare()
): Flow<Any?> =
    map { message ->
        when {
            message.isBlank() -> null
            message[0] == '/' -> context.process(message.drop(1).toString()).prepare().result
            else -> Exec.EnqueueMessage(message.toString())
        }
    }

fun Flow<Any>.translateCli(context: CliContext = CliContext()): Flow<CliContext> =
    scan(context.prepare()) { ctx, input ->
        when (input) {
            is String -> ctx.process(input).prepare()
            else -> ctx
        }
    }
