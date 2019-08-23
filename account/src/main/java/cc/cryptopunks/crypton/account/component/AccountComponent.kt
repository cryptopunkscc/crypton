package cc.cryptopunks.crypton.account.component

import cc.cryptopunks.crypton.account.presentation.fragment.*
import cc.cryptopunks.crypton.core.component.GraphComponent
import cc.cryptopunks.crypton.core.module.ViewModelModule
import cc.cryptopunks.crypton.core.module.ViewModelScope
import cc.cryptopunks.crypton.api.Client
import dagger.Component

@ViewModelScope
@Component(
    dependencies = [GraphComponent::class],
    modules = [
        ViewModelModule::class,
        Client.Module::class
    ]
)
interface AccountComponent : GraphComponent {
    fun inject(target: SetAccountFragment)
    fun inject(target: SignInFragment)
    fun inject(target: SignUpFragment)
    fun inject(target: AccountListFragment)
    fun inject(target: NavigationFragment)
}