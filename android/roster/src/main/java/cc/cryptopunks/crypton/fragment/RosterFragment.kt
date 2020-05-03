package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import cc.cryptopunks.crypton.module.RosterDomainModule
import cc.cryptopunks.crypton.view.RosterView

class RosterFragment : ServiceFragment() {

    override fun onCreatePresenter() = RosterDomainModule(
        core = featureCore
    ).rosterService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RosterView(context!!)
}
