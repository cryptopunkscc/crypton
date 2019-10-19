package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.*
import cc.cryptopunks.crypton.api.Api
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.navigation.Navigation
import cc.cryptopunks.crypton.presentation.PresentationComponent
import cc.cryptopunks.crypton.presenter.CreateChatPresenter
import cc.cryptopunks.crypton.presenter.Presenter
import cc.cryptopunks.crypton.view.CreateChatView
import javax.inject.Singleton

class CreateChatFragment : PresenterFragment<
        CreateChatPresenter.View,
        CreateChatPresenter,
        CreateChatFragment.Component>() {

    @Singleton
    @dagger.Component(dependencies = [
        Address::class,
        Api.Scope::class,
        Chat.Api::class,
        Chat.Repo::class,
        Navigation.Component::class
    ])
    interface Component : Presenter.Component<CreateChatPresenter>

    override suspend fun onCreateComponent(
        component: PresentationComponent
    ): Component = DaggerCreateChatFragment_Component.builder()
        .address(component.address)
        .scope(component.apiScope)
        .api(component)
        .repo(component.chatRepo)
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
    ): View? = CreateChatView(
        context = context!!
    )

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.create_chat, menu)
    }
}

