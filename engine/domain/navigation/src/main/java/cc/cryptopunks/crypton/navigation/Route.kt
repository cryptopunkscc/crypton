package cc.cryptopunks.crypton.navigation

import cc.cryptopunks.crypton.annotation.FeatureScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class Route(
    open val data: MutableMap<String, Any?> = mutableMapOf()
) {
    class Raw(val id: Int) : Route()
    object Dashboard : Route()
    object Roster : Route()
    object SetAccount : Route()
    object Login : Route()
    object Register : Route()
    object AccountList : Route()
    object AccountManagement : Route()
    object CreateChat : Route()
    class Chat(
        data: MutableMap<String, Any?> = mutableMapOf()
    ) : Route(data) {
        var accountId: String by data
        var chatAddress: String by data
    }

    interface Api {
        val navigate: Navigate
        val navigationOutput: Output

        interface Navigate : (Route) -> Unit
        interface Output : Flow<Route>
    }

    @FeatureScope
    class Navigator @Inject constructor() :
        Api.Navigate,
        Api.Output {

        private val scope = MainScope()

        private val channel = ConflatedBroadcastChannel<Route>()

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
}