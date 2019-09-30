package cc.cryptopunks.crypton.presentation.fragment

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.component.ChatComponent
import cc.cryptopunks.crypton.component.DaggerChatComponent
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.module.ApiClientModule
import cc.cryptopunks.crypton.module.BaseFragmentModule
import cc.cryptopunks.crypton.module.ChatModule
import cc.cryptopunks.crypton.util.BaseFragment

abstract class ChatFragment : BaseFragment() {

    val component: ChatComponent by lazy {
        DaggerChatComponent.builder()
            .chatModule(ChatModule(address))
            .featureComponent(baseActivity.featureComponent)
            .apiClientModule(ApiClientModule(Client.Empty(0)))
            .baseFragmentModule(BaseFragmentModule(this))
            .build()
    }

    private val address: Address get() = TODO()
}