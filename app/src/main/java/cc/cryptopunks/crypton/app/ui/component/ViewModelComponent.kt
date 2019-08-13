package cc.cryptopunks.crypton.app.ui.component

import cc.cryptopunks.crypton.xmpp.Xmpp
import cc.cryptopunks.crypton.app.module.ViewModelModule
import cc.cryptopunks.crypton.app.module.ViewModelScope
import cc.cryptopunks.crypton.app.ui.activity.InitialActivity
import cc.cryptopunks.crypton.app.ui.fragment.InitialNavigationFragment
import cc.cryptopunks.crypton.app.ui.fragment.SetAccountFragment
import cc.cryptopunks.crypton.app.ui.fragment.SignInFragment
import cc.cryptopunks.crypton.app.ui.fragment.SignUpFragment
import dagger.Subcomponent

@ViewModelScope
@Subcomponent(
    modules = [
        ViewModelModule::class,
        Xmpp.Module::class
    ]
)
interface ViewModelComponent {
    fun inject(target: InitialActivity)
    fun inject(target: InitialNavigationFragment)
    fun inject(target: SignInFragment)
    fun inject(target: SignUpFragment)
    fun inject(target: SetAccountFragment)

    @Subcomponent.Builder
    interface Builder {
        fun plus(module: Xmpp.Module): Builder
        fun build(): ViewModelComponent
    }
}