package cc.cryptopunks.crypton.fragment

import cc.cryptopunks.crypton.component.ChatComponent
import cc.cryptopunks.crypton.entity.Address

abstract class ChatFragment : CoreFragment() {

    val component: ChatComponent by lazy {
        TODO()
    }

    private val address: Address get() = TODO()
}