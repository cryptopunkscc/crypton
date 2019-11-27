package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.FeatureCore
import cc.cryptopunks.crypton.SessionCore
import cc.cryptopunks.crypton.annotation.SessionScope
import cc.cryptopunks.crypton.entity.Session
import cc.cryptopunks.crypton.navigation.OptionItem
import cc.cryptopunks.crypton.navigation.Route
import cc.cryptopunks.crypton.presenter.CreateChatPresenter
import cc.cryptopunks.crypton.selector.CurrentSessionSelector
import cc.cryptopunks.crypton.service.SessionServices
import dagger.Component
import dagger.Provides

@SessionScope
@Component(
    dependencies = [
        AndroidCore::class,
        Route.Api::class,
        OptionItem.Api::class,
        Session::class
    ],
    modules = [
        CreateChatCore.Binding::class
    ]
)
interface AndroidSessionCore :
    SessionCore,
    SessionServices,
    CreateChatPresenter.Component


object CreateAndroidSessionCore {

    @dagger.Module
    class Module {

        @Provides
        fun currentSession(
            currentSessionSelector: CurrentSessionSelector
        ) = currentSessionSelector()!!

        @Provides
        fun sessionCore(
            androidCore: AndroidCore,
            featureCore: FeatureCore,
            session: Session
        ): SessionCore = DaggerAndroidSessionCore.builder()
            .androidCore(androidCore)
            .api(featureCore as Route.Api)
            .api(featureCore as OptionItem.Api)
            .session(session)
            .build()
    }
}