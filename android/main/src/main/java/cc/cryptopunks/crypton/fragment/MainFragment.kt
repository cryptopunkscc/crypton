package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import cc.cryptopunks.crypton.service.startMainNavigationService
import kotlinx.coroutines.launch

class MainFragment : FeatureFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launch { appScope.startMainNavigationService() }
    }
}
