package cc.cryptopunks.crypton.util.ext

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import cc.cryptopunks.crypton.navigation.Navigate
import cc.cryptopunks.crypton.navigation.Route
import kotlinx.coroutines.flow.collect

suspend fun Navigate.Output.bind(navController: NavController) = collect {
    navController.navigate(it)
}

private fun NavController.navigate(route: Route) {
    val args = route.data.entries
        .map { (key, value) -> key to value }
        .toTypedArray()

    navigate(
        route.resId,
        bundleOf(*args)
    )
}