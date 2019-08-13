package cc.cryptopunks.crypton.app.ui.fragment

import android.os.Bundle
import cc.cryptopunks.crypton.app.ui.viewmodel.InitialViewModel
import cc.cryptopunks.crypton.app.util.BaseFragment
import javax.inject.Inject

class InitialNavigationFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelComponent.inject(this)
    }

    @Inject
    fun init(
        initialViewModel: InitialViewModel
    ) {
        modelDisposables.addAll(initialViewModel())
    }
}