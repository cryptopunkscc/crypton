package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.SessionCore
import cc.cryptopunks.crypton.annotation.SessionScope
import cc.cryptopunks.crypton.entity.Session
import cc.cryptopunks.crypton.navigation.Navigation
import cc.cryptopunks.crypton.presenter.CreateChatPresenter
import cc.cryptopunks.crypton.selector.CurrentSessionSelector
import cc.cryptopunks.crypton.service.SessionServices
import dagger.Component
import dagger.Provides

@SessionScope
@Component(
    dependencies = [
        AndroidCore::class,
        Navigation::class,
        Session::class
    ],
    modules = [
        CreateChatFeature.Binding::class
    ]
)
interface AndroidSessionCore :
    SessionCore,
    SessionServices,
    CreateChatPresenter.Component {


    @dagger.Module
    class Module {

        @Provides
        fun currentSession(
            currentSessionSelector: CurrentSessionSelector
        ) = currentSessionSelector()!!

        @Provides
        fun sessionFeature(
            androidCore: AndroidCore,
            navigation: Navigation,
            session: Session
        ): SessionCore = DaggerAndroidSessionCore.builder()
            .androidCore(androidCore)
            .navigation(navigation)
            .session(session)
            .build()
    }
}