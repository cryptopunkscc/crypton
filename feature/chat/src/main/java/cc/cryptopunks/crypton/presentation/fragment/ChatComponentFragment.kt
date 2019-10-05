package cc.cryptopunks.crypton.presentation.fragment

import cc.cryptopunks.crypton.component.ChatComponent
import cc.cryptopunks.crypton.component.DaggerChatComponent
import cc.cryptopunks.crypton.dagger.DaggerBaseFragmentModule
import cc.cryptopunks.crypton.util.BaseFragment

abstract class ChatComponentFragment : BaseFragment() {

    val component: ChatComponent by lazy {
        DaggerChatComponent
            .builder()
            .featureComponent(baseActivity.featureComponent)
            .daggerBaseFragmentModule(DaggerBaseFragmentModule(this))
            .build()
    }
}