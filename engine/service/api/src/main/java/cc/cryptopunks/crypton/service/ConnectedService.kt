package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Service
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect

class ConnectedService(
    val service: Service
) :
    Service {

    override val coroutineContext = SupervisorJob() + Dispatchers.Default

    private val inputChannel = BroadcastChannel<Any>(Channel.BUFFERED)
    private val outputChannel = BroadcastChannel<Any>(Channel.CONFLATED)

    private val binding = object :
        Service.Binding {
        override val input: Flow<Any> get() = inputChannel.asFlow()
        override val output: suspend (Any) -> Unit = outputChannel::send
    }

    init {
        service.run {
            binding.bind()
        }
    }

    override fun Service.Binding.bind(): Job = launch {
        launch {
            input.collect {
                inputChannel.send(it)
            }
        }
        launch {
            outputChannel.asFlow().collect(output)
        }
    }


    class Manager {

        private val map = mutableMapOf<Any, Service>()

        fun setup(service: Service) = service.run {
            map[id]?.cancel()
            map[id] = service
            ConnectedService(service)
        }

        fun clear() {
            map.values.forEach { it.cancel() }
            map.clear()
        }

        fun cancel(id: Any) = map.remove(id)?.cancel() != null
    }
}