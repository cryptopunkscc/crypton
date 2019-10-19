package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.fragment.DashboardFragment
import cc.cryptopunks.crypton.presentation.PresentationComponent
import dagger.Component

@Component(dependencies = [PresentationComponent::class])
interface DashboardComponent {
    fun inject(target: DashboardFragment)
}