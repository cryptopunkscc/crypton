package cc.cryptopunks.crypton.account.component

import cc.cryptopunks.crypton.account.presentation.fragment.*
import cc.cryptopunks.crypton.core.component.FeatureComponent
import cc.cryptopunks.crypton.core.module.ViewModelModule
import cc.cryptopunks.crypton.core.module.ViewModelScope
import cc.cryptopunks.crypton.api.Client
import dagger.Component

@ViewModelScope
@Component(
    dependencies = [FeatureComponent::class],
    modules = [
        ViewModelModule::class,
        Client.Module::class
    ]
)
interface AccountComponent : FeatureComponent {
    fun inject(target: SetAccountFragment)
    fun inject(target: SignInFragment)
    fun inject(target: SignUpFragment)
    fun inject(target: AccountListFragment)
    fun inject(target: NavigationFragment)
}