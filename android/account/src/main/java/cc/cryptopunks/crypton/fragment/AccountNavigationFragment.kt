package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import cc.cryptopunks.crypton.service.AccountNavigationService
import cc.cryptopunks.crypton.util.ext.resolve
import kotlinx.coroutines.launch

class AccountNavigationFragment : FeatureFragment() {

    private val core
        get() = featureCore
            .resolve<AccountNavigationService.Core>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        core.run {
            launch { accountNavigationService() }
        }
    }
}