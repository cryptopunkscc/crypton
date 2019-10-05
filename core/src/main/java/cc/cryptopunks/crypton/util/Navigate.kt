package cc.cryptopunks.crypton.util

import cc.cryptopunks.crypton.feature.Route
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

interface Navigate :
        (Route) -> Unit,
        (Route, Any) -> Unit {

    interface Output : Flow<Data>

    data class Data(
        val route: Route,
        val param: Any? = null
    )

    @FlowPreview
    @ExperimentalCoroutinesApi
    private class Impl : Navigate,
        Output {
        override fun invoke(route: Route, param: Any) {
            channel.offer(Data(route, param))
        }

        private val channel = BroadcastChannel<Data?>(Channel.CONFLATED)

        override fun invoke(route: Route) {
            channel.offer(Data(route))
        }


        @InternalCoroutinesApi
        override suspend fun collect(collector: FlowCollector<Data>) {
            channel.asFlow()
                .filterNotNull()
                .onEach { channel.offer(null) }
                .collect(collector)
        }
    }

    class Module : Component {
        private val impl = Impl()
        override val navigate: Navigate get() = impl
        override val navigateOutput: Output get() = impl
    }

    interface Component {
        val navigate: Navigate
        val navigateOutput: Output
    }
}