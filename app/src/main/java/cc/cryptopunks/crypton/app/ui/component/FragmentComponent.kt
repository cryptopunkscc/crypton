package cc.cryptopunks.crypton.app.ui.component

import cc.cryptopunks.crypton.app.module.FragmentModule
import cc.cryptopunks.crypton.app.module.FragmentScope
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [FragmentModule::class])
interface FragmentComponent {

    @Subcomponent.Builder
    interface Builder {
        fun plus(module: FragmentModule): Builder
        fun build(): FragmentComponent
    }
}