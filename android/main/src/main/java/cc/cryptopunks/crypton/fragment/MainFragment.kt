package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import cc.cryptopunks.crypton.service.MainNavigationService
import kotlinx.coroutines.launch

class MainFragment : FeatureFragment() {

    private val component get() = feature as MainNavigationService.Component

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launch { component.mainNavigationService() }
    }
}