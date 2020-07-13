package cc.cryptopunks.crypton.translator

import kotlinx.coroutines.flow.Flow
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
