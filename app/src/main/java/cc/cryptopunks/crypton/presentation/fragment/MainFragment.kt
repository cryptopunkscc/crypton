package cc.cryptopunks.crypton.presentation.fragment

import android.os.Bundle
import cc.cryptopunks.crypton.dagger.DaggerFeatureModule
import cc.cryptopunks.crypton.dagger.DaggerViewModelModule
import cc.cryptopunks.crypton.model.ToggleAppServiceModel
import cc.cryptopunks.crypton.module.ViewModelModule
import cc.cryptopunks.crypton.presentation.dagger.AppComponent
import cc.cryptopunks.crypton.presentation.dagger.DaggerAppComponent
import cc.cryptopunks.crypton.presentation.viewmodel.MainNavigationViewModel
import cc.cryptopunks.crypton.util.BaseFragment
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainFragment : BaseFragment() {

    private val component: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .daggerFeatureModule(DaggerFeatureModule(baseActivity.featureComponent))
            .daggerViewModelModule(DaggerViewModelModule(ViewModelModule(baseActivity.featureComponent)))
            .build()!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    @Inject
    fun init(
        mainNavigationViewModel: MainNavigationViewModel,
        toggleAppServiceModel: ToggleAppServiceModel
    ) {
        launch { mainNavigationViewModel() }
        launch { toggleAppServiceModel() }
    }
}