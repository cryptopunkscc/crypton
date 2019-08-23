package cc.cryptopunks.crypton.app.presentation.fragment

import android.os.Bundle
import cc.cryptopunks.crypton.app.presentation.viewmodel.NavigationViewModel
import javax.inject.Inject

class NavigationFragment : AppFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelComponent.inject(this)
    }

    @Inject
    fun init(
        navigationViewModel: NavigationViewModel
    ) {
        modelDisposable.addAll(navigationViewModel())
    }
}