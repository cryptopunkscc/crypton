package cc.cryptopunks.crypton.presentation.fragment

import cc.cryptopunks.crypton.component.ChatComponent
import cc.cryptopunks.crypton.component.DaggerChatComponent
import cc.cryptopunks.crypton.module.fragmentComponent
import cc.cryptopunks.crypton.module.viewModelComponent
import cc.cryptopunks.crypton.util.BaseFragment

abstract class ChatComponentFragment : BaseFragment() {

    val component: ChatComponent by lazy {
        DaggerChatComponent.builder()
            .viewModelComponent(viewModelComponent())
            .fragmentComponent(fragmentComponent())
            .build()
    }
}