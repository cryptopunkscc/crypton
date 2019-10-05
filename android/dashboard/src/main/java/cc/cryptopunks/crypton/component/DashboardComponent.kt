package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.presentation.fragment.DashboardFragment
import dagger.Component

@Component(dependencies = [ViewModelComponent::class])
interface DashboardComponent {
    fun inject(target: DashboardFragment)
}