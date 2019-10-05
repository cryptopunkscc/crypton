package cc.cryptopunks.crypton.presentation.fragment

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.presentation.binding.RosterBinding
import javax.inject.Inject

class RosterFragment : ChatComponentFragment() {

    override val layoutRes: Int get() = R.layout.conversations

    @Inject
    lateinit var binding: RosterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        component.inject(binding.ViewBinding())
    }
}