package cc.cryptopunks.crypton.presentation.fragment

import android.os.Bundle
import cc.cryptopunks.crypton.presentation.viewmodel.AccountNavigationViewModel
import javax.inject.Inject

class NavigationFragment : BaseAccountFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accountComponent.inject(this)
    }

    @Inject
    fun init(
        navigationViewModel: AccountNavigationViewModel
    ) = Unit
}