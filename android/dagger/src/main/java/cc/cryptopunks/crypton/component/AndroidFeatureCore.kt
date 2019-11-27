package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.FeatureCore
import cc.cryptopunks.crypton.annotation.FeatureScope
import cc.cryptopunks.crypton.core.Core
import cc.cryptopunks.crypton.navigation.Navigation
import cc.cryptopunks.crypton.navigation.NavigationModule
import cc.cryptopunks.crypton.presenter.DashboardPresenter
import cc.cryptopunks.crypton.presenter.RosterPresenter
import cc.cryptopunks.crypton.service.AccountNavigationService
import cc.cryptopunks.crypton.service.MainNavigationService
import dagger.Binds
import dagger.Component
import dagger.Module
import javax.inject.Inject

@FeatureScope
@Component(
    dependencies = [
        AndroidCore::class,
        Navigation::class
    ],
    modules = [
        AndroidSessionCore.Module::class
    ]
)
interface AndroidFeatureCore :
    FeatureCore,
    MainNavigationService.Component,
    AccountNavigationService.Component,
    AccountPresentationComponent,
    DashboardPresenter.Component,
    RosterPresenter.Component {

    val core: Core

    @Module
    interface Bindings {
        @Binds
        fun CreateFeature.createFeature() : FeatureCore.Create
    }
}

class CreateFeature @Inject constructor(
    private val androidCore: AndroidCore
) : FeatureCore.Create {
    override fun invoke(): FeatureCore = DaggerAndroidFeatureCore.builder()
        .androidCore(androidCore)
        .navigation(NavigationModule())
        .build()
}