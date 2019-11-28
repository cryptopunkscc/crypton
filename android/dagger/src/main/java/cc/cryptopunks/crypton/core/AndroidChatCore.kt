package cc.cryptopunks.crypton.core

import cc.cryptopunks.crypton.annotation.ChatScope
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.presenter.ChatPresenter
import dagger.Binds
import dagger.Component
import dagger.Provides
import javax.inject.Inject

@ChatScope
@Component(
    dependencies = [
        AndroidSessionCore::class
    ],
    modules = [
        AndroidChatCore.Module::class
    ]
)
interface AndroidChatCore:
    ChatCore,
    ChatPresenter.Core {

    @dagger.Module
    class Module(
        @get:Provides val chat: Chat
    )
}

class CreateChatCore @Inject constructor(
    androidSessionCore: AndroidSessionCore
) : ChatCore.Factory, (Chat) -> ChatCore by { chat ->
    DaggerAndroidChatCore.builder()
        .androidSessionCore(androidSessionCore)
        .module(AndroidChatCore.Module(chat))
        .build()
} {

    @dagger.Module
    interface Binding {
        @Binds
        fun CreateChatCore.createChatCore(): ChatCore.Factory
    }
}