package cc.cryptopunks.crypton.conversation.component

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.conversation.presentation.binding.ConversationListBinding
import cc.cryptopunks.crypton.conversation.presentation.fragment.ConversationListFragment
import cc.cryptopunks.crypton.core.component.GraphComponent
import cc.cryptopunks.crypton.core.module.BaseFragmentModule
import cc.cryptopunks.crypton.core.module.ViewModelModule
import cc.cryptopunks.crypton.core.module.ViewModelScope
import dagger.Component

@ViewModelScope
@Component(
    dependencies = [GraphComponent::class],
    modules = [
        BaseFragmentModule::class,
        ViewModelModule::class,
        Client.Module::class
    ]
)
interface ConversationComponent : GraphComponent {
    fun inject(target: ConversationListFragment)
    fun inject(viewBinding: ConversationListBinding.ViewBinding)
}