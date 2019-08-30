package cc.cryptopunks.crypton.presentation.fragment

import cc.cryptopunks.crypton.component.ConversationComponent
import cc.cryptopunks.crypton.component.DaggerConversationComponent
import cc.cryptopunks.crypton.module.BaseFragmentModule
import cc.cryptopunks.crypton.util.BaseFragment

abstract class BaseConversationFragment : BaseFragment() {

    val component: ConversationComponent by lazy {
        DaggerConversationComponent
            .builder()
            .featureComponent(baseActivity.featureComponent)
            .baseFragmentModule(BaseFragmentModule(this))
            .build()
    }
}