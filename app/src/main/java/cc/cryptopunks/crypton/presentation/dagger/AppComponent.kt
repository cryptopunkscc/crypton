package cc.cryptopunks.crypton.presentation.dagger

import cc.cryptopunks.crypton.component.ViewModelComponent
import cc.cryptopunks.crypton.presentation.fragment.MainFragment
import dagger.Component

@Component(dependencies = [ViewModelComponent::class])
interface AppComponent : ViewModelComponent {
    fun inject(target: MainFragment)
}