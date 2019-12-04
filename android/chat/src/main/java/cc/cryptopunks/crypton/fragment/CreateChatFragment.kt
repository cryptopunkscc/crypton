package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import cc.cryptopunks.crypton.presenter.CreateChatService
import cc.cryptopunks.crypton.util.ext.resolve
import cc.cryptopunks.crypton.view.CreateChatView

class CreateChatFragment : ServiceFragment() {

    override fun onCreatePresenter() = featureCore
        .sessionFeature()
        .resolve<CreateChatService.Core>()
        .createChatService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = CreateChatView(context!!)
}