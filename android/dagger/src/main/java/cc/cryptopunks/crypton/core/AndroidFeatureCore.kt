package cc.cryptopunks.crypton.core

import cc.cryptopunks.crypton.RosterService
import cc.cryptopunks.crypton.annotation.FeatureScope
import cc.cryptopunks.crypton.context.Feature
import cc.cryptopunks.crypton.context.OptionItem
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.internal.Navigator
import cc.cryptopunks.crypton.internal.OptionItemBroadcast
import cc.cryptopunks.crypton.manager.PresenceManager
import cc.cryptopunks.crypton.service.*
import dagger.Binds
import dagger.Component
import dagger.Provides
import javax.inject.Inject

@FeatureScope
@Component(
    dependencies = [
        AndroidCore::class
    ],
    modules = [
        AndroidFeatureCore.Bindings::class,
        AndroidFeatureCore.Module::class,
        AndroidSessionCoreFactory.Module::class
    ]
)
interface AndroidFeatureCore :
    FeatureCore,
    ServiceManager.Core,
    PresenceManager.Core,
    MainNavigationService.Core,
    AccountNavigationService.Core,
    DashboardService.Core,
    RosterService.Core,
    SetAccountService.Core,
    SignInService.Core,
    SignUpService.Core,
    AccountListService.Core {

    val androidCore: AndroidCore
    val featureCore: FeatureCore

    @dagger.Module
    class Module {
        @Provides
        fun featureScope() = Feature.Scope()
    }

    @dagger.Module
    interface Bindings {
        @Binds
        fun AndroidFeatureCore.featureCore(): FeatureCore

        @Binds
        fun Navigator.navigate(): Route.Api.Navigate

        @Binds
        fun Navigator.navigationOutput(): Route.Api.Output

        @Binds
        fun OptionItemBroadcast.selectOptionItem(): OptionItem.Select

        @Binds
        fun OptionItemBroadcast.optionItemOutput(): OptionItem.Output
    }
}

class AndroidFeatureCoreFactory @Inject constructor(
    private val androidCore: AndroidCore
) : FeatureCore.Create {

    override fun invoke() = DaggerAndroidFeatureCore
        .builder()
        .androidCore(androidCore)
        .build()!!

    @dagger.Module
    interface Bindings {
        @Binds
        fun AndroidFeatureCoreFactory.createFeatureCore(): FeatureCore.Create
    }
}