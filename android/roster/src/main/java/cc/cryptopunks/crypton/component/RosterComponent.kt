package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.actor.OptionItemSelected
import cc.cryptopunks.crypton.feature.chat.presenter.RosterPresenter
import cc.cryptopunks.crypton.fragment.RosterView
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(dependencies = [PresentationComponent::class])
interface RosterComponent : OptionItemSelected.Component {
    val presentRoster: RosterPresenter
    val rosterView: RosterView
}