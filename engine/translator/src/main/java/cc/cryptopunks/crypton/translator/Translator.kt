package cc.cryptopunks.crypton.translator

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.scan

fun Flow<Any>.translateCli(context: Context = Context()): Flow<Context> =
    scan(context.prepare()) { ctx, input ->
        when (input) {
            is String -> ctx.process(input).prepare()
            else -> ctx
        }
    }
