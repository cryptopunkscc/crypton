package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import cc.cryptopunks.crypton.core
import cc.cryptopunks.crypton.core.Core
import cc.cryptopunks.crypton.navigation.Navigation
import cc.cryptopunks.crypton.service.MainNavigationService
import kotlinx.coroutines.launch

class MainFragment : CoreFragment() {

    @dagger.Component(
        dependencies = [
            Navigation::class,
            Core::class
        ]
    )
    interface Component : MainNavigationService.Component

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launch {
            DaggerMainFragment_Component.builder()
                .navigation(navigation)
                .core(core)
                .build()
                .mainNavigationService()
        }
    }
}