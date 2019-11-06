package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.ViewGroup
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.presenter.CreateChatPresenter
import cc.cryptopunks.crypton.util.ext.resolve
import cc.cryptopunks.crypton.view.CreateChatView

class CreateChatFragment : PresenterFragment<
        CreateChatPresenter.View,
        CreateChatPresenter>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreatePresenter(): CreateChatPresenter = feature
        .sessionFeature()
        .resolve<CreateChatPresenter.Component>()
        .createChatPresenter

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
    ) = inflater.inflate(
        R.menu.create_chat,
        menu
    )
}