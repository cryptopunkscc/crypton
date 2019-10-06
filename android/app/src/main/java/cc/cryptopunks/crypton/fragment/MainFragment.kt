package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import cc.cryptopunks.crypton.model.ToggleIndicatorServiceModel
import cc.cryptopunks.crypton.module.viewModelComponent
import cc.cryptopunks.crypton.presentation.component.AppComponent
import cc.cryptopunks.crypton.presentation.component.DaggerAppComponent
import cc.cryptopunks.crypton.presentation.viewmodel.MainNavigationViewModel
import cc.cryptopunks.crypton.util.BaseFragment
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainFragment : BaseFragment() {

    private val component: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .viewModelComponent(viewModelComponent())
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    @Inject
    fun init(
        mainNavigationViewModel: MainNavigationViewModel,
        toggleIndicatorServiceModel: ToggleIndicatorServiceModel
    ) {
        launch { mainNavigationViewModel() }
        launch { toggleIndicatorServiceModel() }
    }
}