package cc.cryptopunks.crypton.internal

import cc.cryptopunks.crypton.annotation.FeatureScope
import cc.cryptopunks.crypton.context.Route
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@FeatureScope
class Navigator @Inject constructor() :
    Route.Api.Navigate,
    Route.Api.Output {

    private val scope = MainScope()

    private val channel =
        ConflatedBroadcastChannel<Route>()

    override fun invoke(route: Route) {
        scope.launch {
            channel.send(route)
        }
    }

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<Route>) {
        channel.asFlow().collect(collector)
    }
}