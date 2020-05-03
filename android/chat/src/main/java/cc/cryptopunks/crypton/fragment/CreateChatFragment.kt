package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import cc.cryptopunks.crypton.module.CreateChatServiceModule
import cc.cryptopunks.crypton.view.CreateChatView

class CreateChatFragment : ServiceFragment() {

    override fun onCreatePresenter() = CreateChatServiceModule(
        core = featureCore.sessionCore()
    ).createChatService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = CreateChatView(context!!)
}
