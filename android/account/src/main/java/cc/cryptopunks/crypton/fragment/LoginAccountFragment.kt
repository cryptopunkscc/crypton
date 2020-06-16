package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cc.cryptopunks.crypton.module.AccountDomainModule
import cc.cryptopunks.crypton.view.CreateAccountView

class LoginAccountFragment : ServiceFragment() {

    override fun onCreatePresenter() = AccountDomainModule(
        appScope = appScope
    ).createAccountService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = CreateAccountView(activity!!).login()
}
