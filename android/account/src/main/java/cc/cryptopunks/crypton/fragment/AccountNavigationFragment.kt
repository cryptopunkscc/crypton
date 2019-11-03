package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import cc.cryptopunks.crypton.service.AccountNavigationService
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountNavigationFragment : AccountComponentFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accountComponent.inject(this)
    }

    @Inject
    fun init(
        accountNavigationService: AccountNavigationService
    ) {
        launch { accountNavigationService() }
    }
}