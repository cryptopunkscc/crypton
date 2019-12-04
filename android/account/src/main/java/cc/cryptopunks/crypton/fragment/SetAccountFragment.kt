package cc.cryptopunks.crypton.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cc.cryptopunks.crypton.util.ext.resolve
import cc.cryptopunks.crypton.view.SetAccountView
import cc.cryptopunks.crypton.viewmodel.SetAccountService

class SetAccountFragment : ServiceFragment() {

    override fun onCreatePresenter() = featureCore
        .resolve<SetAccountService.Core>()
        .setAccountService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = SetAccountView(context!!)
}