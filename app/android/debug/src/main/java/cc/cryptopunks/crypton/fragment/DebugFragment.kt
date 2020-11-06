package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import cc.cryptopunks.crypton.service
import cc.cryptopunks.crypton.view.DebugView

class DebugFragment : ServiceFragment() {

    override fun onCreateService() = rootScope.service()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DebugView(baseActivity!!)
}
