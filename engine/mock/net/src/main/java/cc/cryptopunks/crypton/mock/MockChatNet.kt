package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.Chat

class MockChatNet(
    private val state: MockState
) : Chat.Net {

    override fun createChat(chat: Chat): Chat = chat.also {
        state { chatStore reduce { plus(chat.address to chat) } }
    }
}
