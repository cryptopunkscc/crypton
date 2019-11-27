package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.ChatCore
import cc.cryptopunks.crypton.annotation.ChatScope
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.navigation.OptionItem
import cc.cryptopunks.crypton.navigation.Route
import cc.cryptopunks.crypton.presenter.ChatPresenter
import dagger.Binds
import dagger.Component
import dagger.Provides
import javax.inject.Inject

@ChatScope
@Component(
    dependencies = [
        AndroidCore::class,
        Route.Api::class,
        OptionItem.Api::class,
        Session::class
    ],
    modules = [
        AndroidChatCore.Module::class
    ]
)
interface AndroidChatCore:
    ChatCore,
    ChatPresenter.Component {

    @dagger.Module
    class Module(
        @get:Provides val chat: Chat
    )
}

class CreateChatCore @Inject constructor(
    androidCore: AndroidCore,
    routeApi: Route.Api,
    optionItemApi: OptionItem.Api,
    session: Session
) : ChatCore.Factory, (Chat) -> ChatCore by { chat ->
    DaggerAndroidChatCore.builder()
        .androidCore(androidCore)
        .api(routeApi)
        .api(optionItemApi)
        .session(session)
        .module(AndroidChatCore.Module(chat))
        .build()
} {

    @dagger.Module
    interface Binding {
        @Binds
        fun CreateChatCore.createChatCore(): ChatCore.Factory
    }
}