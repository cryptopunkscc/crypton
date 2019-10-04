package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.dagger.ViewModelScope
import cc.cryptopunks.crypton.presentation.fragment.*
import dagger.Component

@ViewModelScope
@Component(dependencies = [FeatureComponent::class])
interface AccountComponent : ViewModelComponent {
    fun inject(target: SetAccountFragment)
    fun inject(target: SignInFragment)
    fun inject(target: SignUpFragment)
    fun inject(target: AccountListFragment)
    fun inject(target: AccountNavigationFragment)
}