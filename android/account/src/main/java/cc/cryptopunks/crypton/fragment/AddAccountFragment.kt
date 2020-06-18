package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cc.cryptopunks.crypton.service.RouterService
import cc.cryptopunks.crypton.view.AddAccountView

class AddAccountFragment : ServiceFragment() {

    override fun onCreatePresenter() =
        RouterService(appScope)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = AddAccountView(context!!)
}
