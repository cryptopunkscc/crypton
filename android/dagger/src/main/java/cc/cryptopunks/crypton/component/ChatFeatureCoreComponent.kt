package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.ChatFeatureCore
import cc.cryptopunks.crypton.annotation.ChatScope
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.Session
import cc.cryptopunks.crypton.navigation.Navigation
import cc.cryptopunks.crypton.presenter.ChatPresenter
import dagger.Binds
import dagger.Component
import dagger.Provides
import javax.inject.Inject

@ChatScope
@Component(
    dependencies = [
        AndroidCore::class,
        Navigation::class,
        Session::class
    ],
    modules = [
        ChatFeatureCoreComponent.Module::class
    ]
)
interface ChatFeatureCoreComponent:
    ChatFeatureCore,
    ChatPresenter.Component {

    @dagger.Module
    class Module(
        @get:Provides val chat: Chat
    )
}

class CreateChatFeature @Inject constructor(
    private val androidCore: AndroidCore,
    private val navigation: Navigation,
    private val session: Session
) : ChatFeatureCore.Factory, (Chat) -> ChatFeatureCore by { chat ->
    DaggerChatFeatureCoreComponent.builder()
        .androidCore(androidCore)
        .navigation(navigation)
        .session(session)
        .module(ChatFeatureCoreComponent.Module(chat))
        .build()
} {

    @dagger.Module
    interface Binding {
        @Binds
        fun CreateChatFeature.createChatFeature(): ChatFeatureCore.Factory
    }
}