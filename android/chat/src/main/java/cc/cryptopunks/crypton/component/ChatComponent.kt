package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.feature.chat.presenter.ChatPresenter
import cc.cryptopunks.crypton.module.ChatModule
import dagger.Component

@Component(
    dependencies = [
        ViewModelComponent::class,
        FragmentComponent::class,
        Client::class
    ],
    modules = [
        ChatModule::class
    ]
)
interface ChatComponent : ViewModelComponent {
    val presentChat: ChatPresenter
}