package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.core.Core
import cc.cryptopunks.crypton.fragment.*
import cc.cryptopunks.crypton.navigation.Navigation
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [
        Navigation::class,
        Core::class
    ]
)
interface AccountComponent {
    fun inject(target: SetAccountFragment)
    fun inject(target: SignInFragment)
    fun inject(target: SignUpFragment)
    fun inject(target: AccountListFragment)
    fun inject(target: AccountNavigationFragment)
}