package cc.cryptopunks.crypton.core

import cc.cryptopunks.crypton.FeatureCore
import cc.cryptopunks.crypton.annotation.FeatureScope
import cc.cryptopunks.crypton.context.Core
import cc.cryptopunks.crypton.navigation.OptionItem
import cc.cryptopunks.crypton.navigation.Route
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
        AndroidCore::class
    ],
    modules = [
        CreateAndroidSessionCore.Module::class,
        AndroidFeatureCore.Bindings::class
    ]
)
interface AndroidFeatureCore :
    FeatureCore,
    MainNavigationService.Core,
    AccountNavigationService.Core,
    AccountPresentationCore,
    DashboardPresenter.Core,
    RosterPresenter.Core {

    val core: Core
    val featureCore: FeatureCore


    @Module
    interface Bindings {
        @Binds
        fun Route.Navigator.navigate() : Route.Api.Navigate
        @Binds
        fun Route.Navigator.navigationOutput() : Route.Api.Output
        @Binds
        fun OptionItem.Broadcast.selectOptionItem() : OptionItem.Select
        @Binds
        fun OptionItem.Broadcast.optionItemOutput() : OptionItem.Output
        @Binds
        fun AndroidFeatureCore.featureCore() : FeatureCore
    }
}

class CreateAndroidFeatureCore @Inject constructor(
    private val androidCore: AndroidCore
) : FeatureCore.Create {

    override fun invoke(): FeatureCore = DaggerAndroidFeatureCore.builder()
        .androidCore(androidCore)
        .build()

    @dagger.Module
    interface Bindings {
        @Binds
        fun CreateAndroidFeatureCore.createFeature() : FeatureCore.Create
    }
}