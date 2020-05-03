package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import cc.cryptopunks.crypton.module.AppDomainModule
import kotlinx.coroutines.launch

class MainFragment : FeatureFragment() {

    private val core get() = AppDomainModule(appCore)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launch { core.mainNavigationService() }
    }
}
