package cc.cryptopunks.crypton.sys

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.util.ext.resId
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

internal class RouteSys : Route.Sys {

    private val scope = MainScope()

    private val channel = ConflatedBroadcastChannel<Route?>()

    override fun navigate(route: Route) {
        scope.launch {
            channel.send(route)
            channel.send(null)
        }
    }

    override suspend fun bind(navigator: Any) {
        navigator as NavController
        channel.asFlow().filterNotNull().collect { route ->
            navigator.navigate(
                route.resId,
                bundleOf(*route.argsArray())
            )
        }
    }

    private fun Route.argsArray() = data.entries
        .map { (key, value) -> key to value }
        .toTypedArray()
}
