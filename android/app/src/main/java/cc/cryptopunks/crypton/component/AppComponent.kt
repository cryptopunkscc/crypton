package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.fragment.MainFragment
import dagger.Component

@Component(dependencies = [ViewModelComponent::class])
interface AppComponent : ViewModelComponent {
    fun inject(target: MainFragment)
}