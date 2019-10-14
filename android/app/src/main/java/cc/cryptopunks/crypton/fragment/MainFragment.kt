package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import cc.cryptopunks.crypton.applicationComponent
import cc.cryptopunks.crypton.component.MainComponent
import cc.cryptopunks.crypton.component.DaggerMainComponent
import cc.cryptopunks.crypton.feature.main.service.MainNavigationService
import cc.cryptopunks.crypton.service.ToggleIndicatorService
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainFragment : CoreFragment() {

    private val component: MainComponent by lazy {
        DaggerMainComponent.factory().create(
            application = applicationComponent,
            navigation = coreActivity.navigationComponent
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    @Inject
    fun init(
        mainNavigationService: MainNavigationService,
        toggleIndicatorService: ToggleIndicatorService
    ) {
        launch { mainNavigationService() }
        launch { toggleIndicatorService() }
    }
}