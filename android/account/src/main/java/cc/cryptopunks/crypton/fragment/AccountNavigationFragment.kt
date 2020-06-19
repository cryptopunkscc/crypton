package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import cc.cryptopunks.crypton.service.handleAccountConnections
import kotlinx.coroutines.launch

class AccountNavigationFragment : FeatureFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launch { appScope.handleAccountConnections() }
    }
}
