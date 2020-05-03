package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import cc.cryptopunks.crypton.module.AccountDomainModule
import kotlinx.coroutines.launch

class AccountNavigationFragment : FeatureFragment() {

    private val accountNavigationService
        get() = AccountDomainModule(appCore).accountNavigationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launch { accountNavigationService() }
    }
}
