package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import cc.cryptopunks.crypton.service.AccountNavigationService
import kotlinx.coroutines.launch

class AccountNavigationFragment : AccountComponentFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (feature as AccountNavigationService.Component).run {
            launch { accountNavigationService() }
        }
    }
}