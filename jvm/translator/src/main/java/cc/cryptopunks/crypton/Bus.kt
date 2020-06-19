package cc.cryptopunks.crypton

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.scan

fun Flow<Any>.translate(context: Context = Context()) =
    scan(context.prepare()) { ctx, input ->
        when(input) {
            is String -> ctx.process(input)
            else -> ctx
        }
    }.mapNotNull {
        it.result
    }
