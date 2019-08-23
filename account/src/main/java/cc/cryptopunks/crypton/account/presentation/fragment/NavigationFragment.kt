package cc.cryptopunks.crypton.account.presentation.fragment

import android.os.Bundle
import cc.cryptopunks.crypton.account.presentation.viewmodel.NavigationViewModel
import javax.inject.Inject

class NavigationFragment : BaseAccountFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accountComponent.inject(this)
    }

    @Inject
    fun init(
        navigationViewModel: NavigationViewModel
    ) {
        modelDisposable.addAll(navigationViewModel())
    }
}