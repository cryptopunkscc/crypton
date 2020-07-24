package cc.cryptopunks.crypton.cli

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.translator.Context
import cc.cryptopunks.crypton.translator.prepare
import cc.cryptopunks.crypton.translator.process
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun Flow<CharSequence>.translateMessageInput(
    context: Context = context().prepare()
): Flow<Any?> =
    map { message ->
        when {
            message.isBlank() -> null
            message[0] == '/' -> context.process(message.drop(1).toString()).prepare().result
            else -> Exec.EnqueueMessage(message.toString())
        }
    }
