package cc.cryptopunks.crypton.core

import cc.cryptopunks.crypton.FeatureCore
import cc.cryptopunks.crypton.annotation.FeatureScope
import cc.cryptopunks.crypton.context.OptionItem
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.manager.PresenceManager
import cc.cryptopunks.crypton.navigation.Navigator
import cc.cryptopunks.crypton.navigation.OptionItemBroadcast
import cc.cryptopunks.crypton.presentation.PresentationManager
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
    PresentationManager.Core,
    PresenceManager.Core,
    MainNavigationService.Core,
    AccountNavigationService.Core,
    AccountPresentationCore,
    DashboardPresenter.Core,
    RosterPresenter.Core {

    val androidCore: AndroidCore
    val featureCore: FeatureCore


    @Module
    interface Bindings {
        @Binds
        fun AndroidFeatureCore.featureCore() : FeatureCore
        @Binds
        fun Navigator.navigate() : Route.Api.Navigate
        @Binds
        fun Navigator.navigationOutput() : Route.Api.Output
        @Binds
        fun OptionItemBroadcast.selectOptionItem() : OptionItem.Select
        @Binds
        fun OptionItemBroadcast.optionItemOutput() : OptionItem.Output
    }
}

class CreateAndroidFeatureCore @Inject constructor(
    private val androidCore: AndroidCore
) : FeatureCore.Create {

    override fun invoke() = DaggerAndroidFeatureCore
        .builder()
        .androidCore(androidCore)
        .build()!!

    @dagger.Module
    interface Bindings {
        @Binds
        fun CreateAndroidFeatureCore.createFeature() : FeatureCore.Create
    }
}