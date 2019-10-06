package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.fragment.ChatFragment
import cc.cryptopunks.crypton.fragment.CreateChatFragment
import cc.cryptopunks.crypton.fragment.RosterFragment
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