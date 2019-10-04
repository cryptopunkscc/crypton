package cc.cryptopunks.crypton.presentation.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.presentation.binding.CreateChatBinding
import javax.inject.Inject

class CreateChatFragment : BaseChatFragment() {

    override val layoutRes: Int get() = R.layout.create_conversation

    @Inject
    lateinit var binding: CreateChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        component.inject(binding.ViewBinding())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.create_chat, menu)
    }
}