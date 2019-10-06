package cc.cryptopunks.crypton.fragment

import cc.cryptopunks.crypton.component.ChatComponent
import cc.cryptopunks.crypton.component.DaggerChatComponent
import cc.cryptopunks.crypton.module.fragmentComponent
import cc.cryptopunks.crypton.module.viewModelComponent

abstract class ChatComponentFragment : CoreFragment() {

    val component: ChatComponent by lazy {
        DaggerChatComponent.builder()
            .viewModelComponent(viewModelComponent())
            .fragmentComponent(fragmentComponent())
            .build()
    }
}