package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.fragment.CreateChatFragment
import cc.cryptopunks.crypton.util.OptionItemSelected
import dagger.Component

@Component(dependencies = [PresentationComponent::class])
interface CreateChatComponent : OptionItemSelected.Component {
    fun inject(target: CreateChatFragment)
}