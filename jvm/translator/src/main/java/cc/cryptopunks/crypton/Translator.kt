package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.Chat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.scan

fun Flow<Any>.translateCli(context: Context = Context()): Flow<Any> =
    scan(context.prepare()) { ctx, input ->
        when (input) {
            is String -> ctx.process(input)
            else -> ctx
        }
    }.mapNotNull {
        it.result
    }

fun Flow<CharSequence>.translateMessageInput(context: Context = Context().prepare()): Flow<Any?> =
    map { message ->
        when {
            message.isBlank() -> null
            message[0] == '/' -> context.process(message.drop(1).toString()).result
            else -> Chat.Service.EnqueueMessage(message.toString())
        }
    }
