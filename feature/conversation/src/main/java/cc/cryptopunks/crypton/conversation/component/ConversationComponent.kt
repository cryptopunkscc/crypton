package cc.cryptopunks.crypton.conversation.component

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.conversation.presentation.binding.ConversationListBinding
import cc.cryptopunks.crypton.conversation.presentation.fragment.ConversationListFragment
import cc.cryptopunks.crypton.component.FeatureComponent
import cc.cryptopunks.crypton.module.BaseFragmentModule
import cc.cryptopunks.crypton.module.ViewModelModule
import cc.cryptopunks.crypton.module.ViewModelScope
import dagger.Component

@ViewModelScope
@Component(
    dependencies = [FeatureComponent::class],
    modules = [
        BaseFragmentModule::class,
        ViewModelModule::class,
        Client.Module::class
    ]
)
interface ConversationComponent : FeatureComponent {
    fun inject(target: ConversationListFragment)
    fun inject(viewBinding: ConversationListBinding.ViewBinding)
}