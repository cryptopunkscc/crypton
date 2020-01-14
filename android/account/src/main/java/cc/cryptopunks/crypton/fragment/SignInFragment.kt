package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import cc.cryptopunks.crypton.service.SignInService
import cc.cryptopunks.crypton.util.ext.resolve
import cc.cryptopunks.crypton.view.SignView

class SignInFragment : ServiceFragment() {

    override fun onCreatePresenter() = featureCore
        .resolve<SignInService.Core>()
        .signInService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = SignView(activity!!).login()
}