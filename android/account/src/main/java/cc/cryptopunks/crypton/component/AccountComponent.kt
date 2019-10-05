package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.presentation.fragment.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(dependencies = [ViewModelComponent::class])
interface AccountComponent {
    fun inject(target: SetAccountFragment)
    fun inject(target: SignInFragment)
    fun inject(target: SignUpFragment)
    fun inject(target: AccountListFragment)
    fun inject(target: AccountNavigationFragment)
}