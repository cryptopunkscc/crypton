package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import cc.cryptopunks.crypton.service.SignUpService
import cc.cryptopunks.crypton.util.ext.resolve
import cc.cryptopunks.crypton.view.SignView

class SignUpFragment : ServiceFragment() {

    override fun onCreatePresenter() = featureCore
        .resolve<SignUpService.Core>()
        .signUpService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = SignView(activity!!).register()
}