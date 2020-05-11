package cc.cryptopunks.crypton.mock.net

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.mock.MockState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class ChatNetMock(
    private val state: MockState
) : Chat.Net {

    override val createChat = object : Chat.Net.Create {
        override fun invoke(chat: Chat): Chat = chat.also {
            state { chatStore reduce { plus(chat.address to chat) } }
        }
    }
    override val multiUserChatFlow: Chat.Net.MultiUserChatFlow =
        object : Chat.Net.MultiUserChatFlow, Flow<Chat> by emptyFlow() {}

    override val multiUserChatList = object :
        Chat.Net.MultiUserChatList {
        override fun invoke(): List<Chat> =
            emptyList()
    }
}
