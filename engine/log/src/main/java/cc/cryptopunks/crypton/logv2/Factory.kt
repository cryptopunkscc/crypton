package cc.cryptopunks.crypton.logv2

import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

private val logKey = LogElement<Unit, Any> { _, _ -> }.key

val CoroutineScope.log: Log<Unit, Any> get() = coroutineContext[logKey]!!

val basicLog = createLog<Unit, Any> { build ->
    Unit.build()
}

fun coroutineLog(
    context: CoroutineContext,
) = coroutineLog(CoroutineScope(context))

fun coroutineLog(
    scope: CoroutineScope,
) = createLog<CoroutineScope, Any> { build ->
    scope.build()
}

fun legacyCoroutineLog(
    context: CoroutineContext,
) = createLog<Unit, Any> { build ->
    log(
        context = context,
        build = { Unit.build() },
        timestamp = System.currentTimeMillis()
    )
}

fun legacyCoroutineLogBuilder(
    context: CoroutineContext,
) = createLog<LegacyLogEventBuilder, Unit> { build ->
    log(
        context = context,
        level = cc.cryptopunks.crypton.util.Log.Level.Debug,
        build = { build() },
        timestamp = System.currentTimeMillis()
    )
}
