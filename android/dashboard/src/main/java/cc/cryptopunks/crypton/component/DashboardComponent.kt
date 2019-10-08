package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.fragment.DashboardFragment
import dagger.Component

@Component(dependencies = [PresentationComponent::class])
interface DashboardComponent {
    fun inject(target: DashboardFragment)
}