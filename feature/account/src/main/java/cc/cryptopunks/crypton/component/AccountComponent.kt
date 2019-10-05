package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.presentation.fragment.*
import dagger.Component

@Component(dependencies = [FeatureComponent::class])
interface AccountComponent : ViewModelComponent {
    fun inject(target: SetAccountFragment)
    fun inject(target: SignInFragment)
    fun inject(target: SignUpFragment)
    fun inject(target: AccountListFragment)
    fun inject(target: AccountNavigationFragment)
}