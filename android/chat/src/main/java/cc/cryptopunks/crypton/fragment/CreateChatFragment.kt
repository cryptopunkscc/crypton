package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.*
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.coreComponent
import cc.cryptopunks.crypton.presentation.PresentationComponent
import cc.cryptopunks.crypton.presenter.CreateChatPresenter
import cc.cryptopunks.crypton.presenter.DaggerCreateChatPresenter_Component
import cc.cryptopunks.crypton.repo.repo
import cc.cryptopunks.crypton.view.CreateChatView

class CreateChatFragment : PresenterFragment<
        CreateChatPresenter.View,
        CreateChatPresenter,
        CreateChatPresenter.Component>() {

    override suspend fun onCreateComponent(
        component: PresentationComponent
    ) = DaggerCreateChatPresenter_Component.builder()
        .address(component.address)
        .scope(component.apiScope)
        .net(component.net())
        .repo(coreComponent.repo())
        .component(navigationComponent)
        .build()!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = CreateChatView(
        context = context!!
    )

    override fun onCreateOptionsMenu(
        menu: Menu,
        inflater: MenuInflater
    ) = inflater
        .inflate(R.menu.create_chat, menu)
}

