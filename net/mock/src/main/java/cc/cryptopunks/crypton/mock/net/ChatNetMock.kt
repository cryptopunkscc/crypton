package cc.cryptopunks.crypton.mock.net

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.mock.MockState

class ChatNetMock(
    private val state: MockState
) : Chat.Net {

    override fun createChat(chat: Chat): Chat = chat.also {
        state { chatStore reduce { plus(chat.address to chat) } }
    }
}
