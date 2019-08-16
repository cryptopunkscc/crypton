package cc.cryptopunks.crypton.app.ui.component

import cc.cryptopunks.crypton.app.module.ViewModelModule
import cc.cryptopunks.crypton.app.module.ViewModelScope
import cc.cryptopunks.crypton.app.ui.fragment.*
import cc.cryptopunks.crypton.xmpp.Xmpp
import dagger.Subcomponent

@ViewModelScope
@Subcomponent(
    modules = [
        ViewModelModule::class,
        Xmpp.Module::class
    ]
)
interface ViewModelComponent {
    fun inject(target: InitialNavigationFragment)
    fun inject(target: SetAccountFragment)
    fun inject(target: SignInFragment)
    fun inject(target: SignUpFragment)
    fun inject(target: ChatListFragment)
    fun inject(target: AccountListFragment)

    @Subcomponent.Builder
    interface Builder {
        fun plus(module: Xmpp.Module): Builder
        fun build(): ViewModelComponent
    }
}