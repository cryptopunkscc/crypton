package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import kotlinx.coroutines.flow.Flow

class MockChatNet(
    private val state: MockState
) : Chat.Net {
    override fun createConference(chat: Chat): Chat = chat.also {
        state { chatStore reduce { plus(chat.address to chat) } }
    }

    override fun inviteToConference(chat: Address, users: List<Address>) {
        TODO("Not yet implemented")
    }

    override fun conferenceInvitationsFlow(): Flow<Chat.Net.ConferenceInvitation> {
        TODO("Not yet implemented")
    }

    override fun supportEncryption(address: Address): Boolean {
        TODO("Not yet implemented")
    }

    override fun joinConference(address: Address, nickname: String) {
        TODO("Not yet implemented")
    }
}
