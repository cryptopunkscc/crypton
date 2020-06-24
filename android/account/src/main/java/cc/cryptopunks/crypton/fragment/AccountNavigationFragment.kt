package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.selector.newAccountConnectedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AccountNavigationFragment : FeatureFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launch {
            appScope.newAccountConnectedFlow().collect {
                findNavController().navigate(R.id.navigateAccountList)
            }
        }
    }
}
