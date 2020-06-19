package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cc.cryptopunks.crypton.service.AccountService
import cc.cryptopunks.crypton.view.CreateAccountView

class RegisterAccountFragment : ServiceFragment() {

    override fun onCreateService() =
        AccountService(appScope)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = CreateAccountView(activity!!).register()
}
