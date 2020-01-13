package cc.cryptopunks.crypton.core

import cc.cryptopunks.crypton.annotation.SessionScope
import cc.cryptopunks.crypton.context.Connection
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.service.CreateChatService
import cc.cryptopunks.crypton.selector.CurrentSessionSelector
import cc.cryptopunks.crypton.service.SessionServices
import dagger.Component
import dagger.Provides

@SessionScope
@Component(
    dependencies = [
        AndroidFeatureCore::class,
        Session::class
    ],
    modules = [
        AndroidChatCoreFactory.Binding::class
    ]
)
interface AndroidSessionCore :
    Connection,
    SessionCore,
    SessionServices,
    CreateChatService.Core


object AndroidSessionCoreFactory {

    @dagger.Module
    class Module {

        @Provides
        fun currentSession(
            currentSessionSelector: CurrentSessionSelector
        ) = currentSessionSelector()!!

        @Provides
        fun sessionCore(
            androidFeatureCore: AndroidFeatureCore,
            session: Session
        ): SessionCore = DaggerAndroidSessionCore.builder()
            .androidFeatureCore(androidFeatureCore)
            .session(session)
            .build()
    }
}