package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import cc.cryptopunks.crypton.presenter.RosterService
import cc.cryptopunks.crypton.util.ext.resolve
import cc.cryptopunks.crypton.view.RosterView

class RosterFragment : ServiceFragment() {

    override fun onCreatePresenter() = featureCore
        .resolve<RosterService.Core>()
        .rosterService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RosterView(context!!)
}