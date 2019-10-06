package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import cc.cryptopunks.crypton.component.AppComponent
import cc.cryptopunks.crypton.component.DaggerAppComponent
import cc.cryptopunks.crypton.feature.main.model.MainNavigationModel
import cc.cryptopunks.crypton.model.ToggleIndicatorServiceModel
import cc.cryptopunks.crypton.module.viewModelComponent
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainFragment : CoreFragment() {

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
        mainNavigationModel: MainNavigationModel,
        toggleIndicatorServiceModel: ToggleIndicatorServiceModel
    ) {
        launch { mainNavigationModel() }
        launch { toggleIndicatorServiceModel() }
    }
}