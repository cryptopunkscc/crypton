package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.fragment.RosterFragment
import cc.cryptopunks.crypton.util.OptionItemSelected
import dagger.Component

@Component(dependencies = [PresentationComponent::class])
interface RosterComponent : OptionItemSelected.Component {
    fun inject(target: RosterFragment)
}