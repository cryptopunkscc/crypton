package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.presentation.fragment.*
import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.module.ViewModelModule
import cc.cryptopunks.crypton.module.ViewModelScope
import dagger.Component

@ViewModelScope
@Component(
    dependencies = [FeatureComponent::class],
    modules = [
        ViewModelModule::class,
        Client.Module::class
    ]
)
interface AccountComponent : ViewModelComponent {
    fun inject(target: SetAccountFragment)
    fun inject(target: SignInFragment)
    fun inject(target: SignUpFragment)
    fun inject(target: AccountListFragment)
    fun inject(target: AccountNavigationFragment)
}