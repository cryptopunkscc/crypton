package cc.cryptopunks.crypton.presentation.fragment

import android.os.Bundle
import cc.cryptopunks.crypton.presentation.viewmodel.MainNavigationViewModel
import cc.cryptopunks.crypton.presentation.viewmodel.ToggleAppServiceViewModel
import javax.inject.Inject

class MainFragment : BaseAppFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelComponent.inject(this)
    }

    @Inject
    fun init(
        navigationViewModel: MainNavigationViewModel,
        toggleAppServiceViewModel: ToggleAppServiceViewModel
    ) {
        modelDisposable.addAll(
            navigationViewModel(),
            toggleAppServiceViewModel()
        )
    }
}