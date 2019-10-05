package cc.cryptopunks.crypton.util.ext

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavController
import cc.cryptopunks.crypton.util.Navigate
import kotlinx.coroutines.flow.collect

suspend fun Navigate.Output.bind(navController: NavController) = collect { data ->
    val bundle = when (val param = data.param) {
        is Bundle -> param
        is Parcelable -> Bundle().apply { putParcelable(null, param) }
        else -> null
    }
    navController.navigate(
        data.route.resId,
        bundle
    )
}