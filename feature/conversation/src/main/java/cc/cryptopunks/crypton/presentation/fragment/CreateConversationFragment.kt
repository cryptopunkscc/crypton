package cc.cryptopunks.crypton.presentation.fragment

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.presentation.binding.CreateConversationBinding
import javax.inject.Inject

class CreateConversationFragment : BaseConversationFragment() {

    @Inject
    lateinit var binding: CreateConversationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        component.inject(binding.ViewBinding())
    }
}