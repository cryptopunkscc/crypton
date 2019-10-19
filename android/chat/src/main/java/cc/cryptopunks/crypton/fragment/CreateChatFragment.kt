package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.*
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.component.PresentationComponent
import cc.cryptopunks.crypton.presenter.CreateChatPresenter
import cc.cryptopunks.crypton.presenter.Presenter
import cc.cryptopunks.crypton.view.CreateChatView
import javax.inject.Singleton

class CreateChatFragment : PresenterFragment<
        CreateChatPresenter.View,
        CreateChatPresenter,
        CreateChatFragment.Component>() {

    @Singleton
    @dagger.Component(dependencies = [PresentationComponent::class])
    interface Component : Presenter.Component<CreateChatPresenter>

    override suspend fun onCreateComponent(
        component: PresentationComponent
    ): Component = DaggerCreateChatFragment_Component.builder()
        .presentationComponent(component)
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

