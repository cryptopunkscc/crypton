package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import cc.cryptopunks.crypton.service.RosterService
import cc.cryptopunks.crypton.view.RosterView

class RosterFragment : ServiceFragment() {

    override fun onCreatePresenter() =
        RosterService(appCore)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RosterView(context!!)
}
