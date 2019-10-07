package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.fragment.CreateChatFragment
import cc.cryptopunks.crypton.fragment.RosterFragment
import dagger.Component

@Component(
    dependencies = [
        ViewModelComponent::class,
        FragmentComponent::class
    ]
)
interface RosterComponent: ViewModelComponent {
    fun inject(target: RosterFragment)
    fun inject(target: CreateChatFragment)
}