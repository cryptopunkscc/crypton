package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.ViewGroup
import cc.cryptopunks.crypton.androidCore
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.core
import cc.cryptopunks.crypton.core.Core
import cc.cryptopunks.crypton.entity.Session
import cc.cryptopunks.crypton.navigation.Navigation
import cc.cryptopunks.crypton.presenter.CreateChatPresenter
import cc.cryptopunks.crypton.presenter.Presenter
import cc.cryptopunks.crypton.view.CreateChatView

class CreateChatFragment : PresenterFragment<
        CreateChatPresenter.View,
        CreateChatPresenter>() {

    @dagger.Component(
        dependencies = [
            Core::class,
            Navigation::class,
            Session::class
        ]
    )
    interface Component : Presenter.Component<CreateChatPresenter>

    override fun onCreatePresenter(): CreateChatPresenter =
        DaggerCreateChatFragment_Component.builder()
            .core(core)
            .navigation(navigation)
            .session(androidCore.currentSession())
            .build()
            .presenter

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

