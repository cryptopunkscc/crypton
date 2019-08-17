package cc.cryptopunks.crypton.app.presentation.fragment

import android.os.Bundle
import cc.cryptopunks.crypton.app.presentation.viewmodel.InitialViewModel
import javax.inject.Inject

class InitialNavigationFragment : AppFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelComponent.inject(this)
    }

    @Inject
    fun init(
        initialViewModel: InitialViewModel
    ) {
        modelDisposable.addAll(initialViewModel())
    }
}