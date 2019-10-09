package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.component.DaggerRosterComponent
import cc.cryptopunks.crypton.component.RosterComponent
import cc.cryptopunks.crypton.util.invoke
import kotlinx.coroutines.launch

class RosterFragment : CoreFragment() {

    override val layoutRes: Int get() = R.layout.roster

    private val component: RosterComponent by lazy {
        DaggerRosterComponent.builder()
            .presentationComponent(presentationComponent)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?): Unit = with(component) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        launch { presentRoster() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) : Unit = with(component) {
        launch { presentRoster(rosterView) }
    }
}