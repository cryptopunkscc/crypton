package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import cc.cryptopunks.crypton.coreComponent
import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.navigation.Navigation
import cc.cryptopunks.crypton.repo.repo
import cc.cryptopunks.crypton.service.MainNavigationService
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainFragment : CoreFragment() {


    @dagger.Component(
        dependencies = [
            Account.Repo::class,
            Navigation.Component::class
        ]
    )
    interface Component {
        fun inject(target: MainFragment)
    }

    private val component: Component by lazy {
        DaggerMainFragment_Component.builder()
            .repo(coreComponent.repo())
            .component(navigationComponent)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    @Inject
    fun init(
        mainNavigationService: MainNavigationService
    ) {
        launch { mainNavigationService() }
    }
}