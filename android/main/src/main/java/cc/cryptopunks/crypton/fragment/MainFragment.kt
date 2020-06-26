package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import cc.cryptopunks.crypton.main.R
import cc.cryptopunks.crypton.selector.hasAccountsFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainFragment : FeatureFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launch {
            appScope.hasAccountsFlow().map { (has) ->
                when (has) {
                    true -> baseActivity?.toolbar?.visibility = View.VISIBLE
                    false -> baseActivity?.toolbar?.visibility = View.GONE
                }
                when (has) {
                    true -> R.id.navigateRoster
                    false -> R.id.navigateAddAccount
                }
            }.collect {
                findNavController().navigate(it)
            }
        }
    }
}
