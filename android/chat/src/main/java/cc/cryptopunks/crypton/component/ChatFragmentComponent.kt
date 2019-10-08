package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.factory.CreateChatComponent
import dagger.Component

@Component(dependencies = [PresentationComponent::class])
interface ChatFragmentComponent {
    val createChatComponent: CreateChatComponent
}