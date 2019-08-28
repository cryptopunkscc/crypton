package cc.cryptopunks.crypton.app.presentation.fragment

import android.os.Bundle
import cc.cryptopunks.crypton.app.presentation.viewmodel.NavigationViewModel
import cc.cryptopunks.crypton.app.presentation.viewmodel.ToggleAppServiceViewModel
import javax.inject.Inject

class MainFragment : BaseAppFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelComponent.inject(this)
    }

    @Inject
    fun init(
        navigationViewModel: NavigationViewModel,
        toggleAppServiceViewModel: ToggleAppServiceViewModel
    ) {
        modelDisposable.addAll(
            navigationViewModel(),
            toggleAppServiceViewModel()
        )
    }
}