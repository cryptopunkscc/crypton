package cc.cryptopunks.crypton.conversation.presentation.fragment

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.conversation.R
import cc.cryptopunks.crypton.conversation.presentation.binding.ConversationListBinding
import javax.inject.Inject

class ConversationListFragment : BaseFragment() {

    override val layoutId: Int get() = R.layout.conversations

    @Inject
    lateinit var binding: ConversationListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        component.inject(binding.ViewBinding())
    }
}