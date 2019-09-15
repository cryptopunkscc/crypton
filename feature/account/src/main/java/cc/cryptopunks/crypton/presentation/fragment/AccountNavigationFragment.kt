package cc.cryptopunks.crypton.presentation.fragment

import android.os.Bundle
import cc.cryptopunks.crypton.presentation.viewmodel.AccountNavigationViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountNavigationFragment : AccountComponentFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accountComponent.inject(this)
    }

    @Inject
    fun init(
        navigationViewModel: AccountNavigationViewModel
    ) {
        launch { navigationViewModel() }
    }
}