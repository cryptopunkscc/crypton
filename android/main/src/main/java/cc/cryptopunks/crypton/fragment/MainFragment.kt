package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import cc.cryptopunks.crypton.service.MainNavigationService
import kotlinx.coroutines.launch

class MainFragment : FeatureFragment() {

    private val core get() = featureCore as MainNavigationService.Core

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launch { core.mainNavigationService() }
    }
}