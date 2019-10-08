package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.fragment.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(dependencies = [PresentationComponent::class])
interface AccountComponent {
    fun inject(target: SetAccountFragment)
    fun inject(target: SignInFragment)
    fun inject(target: SignUpFragment)
    fun inject(target: AccountListFragment)
    fun inject(target: AccountNavigationFragment)
}