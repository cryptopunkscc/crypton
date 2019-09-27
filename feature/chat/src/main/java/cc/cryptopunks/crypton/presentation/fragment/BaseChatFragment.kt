package cc.cryptopunks.crypton.presentation.fragment

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.component.ChatComponent
import cc.cryptopunks.crypton.component.DaggerChatComponent
import cc.cryptopunks.crypton.module.ApiClientModule
import cc.cryptopunks.crypton.module.BaseFragmentModule
import cc.cryptopunks.crypton.util.BaseFragment

abstract class BaseChatFragment : BaseFragment() {

    val component: ChatComponent by lazy {
        DaggerChatComponent
            .builder()
            .featureComponent(baseActivity.featureComponent)
            .baseFragmentModule(BaseFragmentModule(this))
            .apiClientModule(ApiClientModule(Client.Empty(0)))
            .build()
    }
}