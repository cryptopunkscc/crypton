package cc.cryptopunks.crypton.presentation.fragment

import cc.cryptopunks.crypton.component.ChatComponent
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.util.BaseFragment

abstract class ChatFragment : BaseFragment() {

    val component: ChatComponent by lazy {
        TODO()
    }

    private val address: Address get() = TODO()
}