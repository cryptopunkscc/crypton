package cc.cryptopunks.crypton.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cc.cryptopunks.crypton.module.CommonDomainModule
import cc.cryptopunks.crypton.view.AddAccountView

class AddAccountFragment : ServiceFragment() {

    override fun onCreatePresenter() = CommonDomainModule(
        appCore = appCore
    ).routerService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = AddAccountView(context!!)
}
