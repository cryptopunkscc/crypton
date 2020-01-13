package cc.cryptopunks.crypton.core

import cc.cryptopunks.crypton.annotation.ChatScope
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.service.ChatService
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
    ChatService.Core {

    @dagger.Module
    class Module(
        @get:Provides val chat: Chat
    )
}

class AndroidChatCoreFactory @Inject constructor(
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
        fun AndroidChatCoreFactory.createChatCore(): ChatCore.Factory
    }
}