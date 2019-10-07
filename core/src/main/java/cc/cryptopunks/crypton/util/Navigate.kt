package cc.cryptopunks.crypton.util

import cc.cryptopunks.crypton.feature.Route
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

interface Navigate : (Route) -> Unit {

    interface Output : Flow<Route>

    operator fun <R: Route>invoke(route: R, init: R.() -> Unit) = invoke(route.apply(init))

    @FlowPreview
    @ExperimentalCoroutinesApi
    private class Impl : Navigate,

        Output {
        private val channel = BroadcastChannel<Route?>(Channel.CONFLATED)

        override fun invoke(route: Route) {
            channel.offer(route)
        }

        @InternalCoroutinesApi
        override suspend fun collect(collector: FlowCollector<Route>) {
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