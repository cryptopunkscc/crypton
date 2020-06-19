package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import cc.cryptopunks.crypton.service.rosterService
import cc.cryptopunks.crypton.view.RosterView

class RosterFragment : ServiceFragment() {

    override fun onCreateService() =
        rosterService(appScope)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RosterView(context!!)
}
