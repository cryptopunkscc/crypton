package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.presentation.fragment.ChatFragment
import cc.cryptopunks.crypton.presentation.fragment.CreateChatFragment
import cc.cryptopunks.crypton.presentation.fragment.RosterFragment
import dagger.Component

@Component(
    dependencies = [
        ViewModelComponent::class,
        FragmentComponent::class
    ]
)
interface ChatComponent: ViewModelComponent {
    fun inject(target: RosterFragment)
    fun inject(target: CreateChatFragment)
    fun inject(target: ChatFragment)
}