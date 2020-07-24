package cc.cryptopunks.crypton.fragment

import android.view.View
import androidx.navigation.fragment.findNavController
import cc.cryptopunks.crypton.main.R
import cc.cryptopunks.crypton.selector.hasAccountsFlow
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SplashFragment : FeatureFragment() {

    override fun onStart() {
        super.onStart()
        launch {
            rootScope.hasAccountsFlow().map { (has) ->
                when (has) {
                    true -> baseActivity?.toolbar?.visibility = View.VISIBLE
                    false -> baseActivity?.toolbar?.visibility = View.GONE
                }
                when (has) {
                    true -> R.id.navigateRoster
                    false -> R.id.navigateAddAccount
                }
            }.collect { destination ->
                findNavController().navigate(destination)
            }
        }
    }

    override fun onStop() {
        coroutineContext.cancelChildren()
        super.onStop()
    }
}
