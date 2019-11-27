package cc.cryptopunks.crypton.util.ext

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import cc.cryptopunks.crypton.navigation.Route
import kotlinx.coroutines.flow.collect

suspend fun Route.Api.Output.bind(navController: NavController) = collect { route ->
    navController.navigate(route)
}

private fun NavController.navigate(route: Route) = navigate(
    route.resId,
    bundleOf(*route.argsArray())
)

private fun Route.argsArray() = data.entries
    .map { (key, value) -> key to value }
    .toTypedArray()