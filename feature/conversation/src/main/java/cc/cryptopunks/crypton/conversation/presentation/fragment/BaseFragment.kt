package cc.cryptopunks.crypton.conversation.presentation.fragment

import cc.cryptopunks.crypton.conversation.component.ConversationComponent
import cc.cryptopunks.crypton.conversation.component.DaggerConversationComponent
import cc.cryptopunks.crypton.core.module.BaseFragmentModule
import cc.cryptopunks.crypton.core.util.BaseFragment

abstract class BaseFragment : BaseFragment() {

    val component: ConversationComponent by lazy {
        DaggerConversationComponent
            .builder()
            .featureComponent(baseActivity.featureComponent)
            .baseFragmentModule(BaseFragmentModule(this))
            .build()
    }
}