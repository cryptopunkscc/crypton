package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import kotlinx.coroutines.flow.Flow

class MockChatNet(
    private val state: MockState
) : Chat.Net {

    override fun createMuc(chat: Chat): Chat = chat.also {
        state { chatStore reduce { plus(chat.address to chat) } }
    }

    override fun mucInvitationsFlow(): Flow<Chat.Net.MucInvitation> {
        TODO("Not yet implemented")
    }

    override fun joinMuc(address: Address) {
        TODO("Not yet implemented")
    }
}
