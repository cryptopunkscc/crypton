package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.presentation.binding.CreateChatBinding
import cc.cryptopunks.crypton.presentation.binding.RosterBinding
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
interface ChatComponent {
    fun inject(target: RosterFragment)
    fun inject(target: RosterBinding.ViewBinding)
    fun inject(target: CreateChatFragment)
    fun inject(target: CreateChatBinding.ViewBinding)
    fun inject(target: ChatFragment)
}