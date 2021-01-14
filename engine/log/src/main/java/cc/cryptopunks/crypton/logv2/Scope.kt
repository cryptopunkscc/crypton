package cc.cryptopunks.crypton.logv2

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

object LogScope : CoroutineScope, LogOutput, LogBroadcast {
    var level: LogLevel = LogLevel.Debug
    override val coroutineContext = SupervisorJob() + newSingleThreadContext("CoroutineLog")
    override fun connect(vararg outputs: LogOutput) = connect(outputs)
    override fun invoke(event: LogEvent) = send(event)
    fun flow(): Flow<LogEvent> = channel.asFlow()
}

private val channel = BroadcastChannel<LogEvent>(2048)

private fun LogScope.send(event: LogEvent) {
    launch { channel.send(event) }
}

private fun connect(outputs: Array<out LogOutput>) =
    LogScope.launch {
        outputs.map { output ->
            LogScope.launch {
                channel.asFlow().collect {
                    output(it)
                }
            }
        }.joinAll()
    }
