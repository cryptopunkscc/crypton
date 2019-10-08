package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.feature.chat.presenter.ChatPresenter
import cc.cryptopunks.crypton.module.ChatModule
import cc.cryptopunks.crypton.view.ChatView
import dagger.Component

@Component(
    dependencies = [
        Client::class,
        PresentationComponent::class
    ],
    modules = [
        ChatModule::class
    ]
)
interface ChatComponent {
    val presentChat: ChatPresenter
    val chatView: ChatView
}