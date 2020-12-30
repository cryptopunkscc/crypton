package cc.cryptopunks.crypton.log

import cc.cryptopunks.crypton.Request
import cc.cryptopunks.crypton.request
import cc.cryptopunks.crypton.util.Instance
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

object LogBuilder {
    operator fun Any.plus(message: Any) = listOf(this, message)
}

data class BuildLog(val build: LogBuilder.() -> Any) : (LogBuilder) -> Any by build

fun Request.log(build: LogBuilder.() -> Any) = CoroutineLog.launch {
    log(Log.Event(this@log, build))
}

suspend fun log(build: LogBuilder.() -> Any) {
    val context = coroutineContext
    CoroutineLog.launch {
        log(Log.Event(CoroutineScope(context).request, build))
    }
}

fun log(event: Log.Event) = CoroutineLog { event.build() }

object Log {

    data class Event(
        val request: Request,
        val element: Any,
        val timestamp: Long = System.currentTimeMillis(),
    ) : Instance

    interface Element : Instance

    data class Status(val name: String) : Element
    data class Data(val any: Any) : Element
    data class Message(val string: String) : Element
    data class Error(val throwable: Throwable) : Element
}

private fun Log.Event.build() = when (element) {
    is BuildLog -> copy(element = element.invoke(LogBuilder))
    else -> this
}


private fun Any.asLogElement(): CoroutineContext = let { event ->
    when (event) {
        is CoroutineContext -> event
        is String -> Log.Message(event)
        is Throwable -> Log.Error(event)
        else -> Log.Data(event)
    }
}

suspend fun Flow<CoroutineContext>.reduce() = reduce { accumulator, value -> accumulator + value }
