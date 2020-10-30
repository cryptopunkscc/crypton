package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import kotlinx.coroutines.flow.Flow

class MockChatNet(
    private val state: MockState
) : Chat.Net {

    override fun inviteToConference(chat: Address, users: Set<Address>) {
        TODO("Not yet implemented")
    }

    override fun conferenceInvitationsFlow(): Flow<Chat.Invitation> {
        TODO("Not yet implemented")
    }

    override fun joinConference(address: Address, nickname: String, historySince: Long) {
        TODO("Not yet implemented")
    }

    override fun supportEncryption(address: Address): Boolean {
        TODO("Not yet implemented")
    }

    override fun createOrJoinConference(chat: Chat): Chat {
        TODO("Not yet implemented")
    }

    override fun configureConference(chat: Address) {
        TODO("Not yet implemented")
    }

    override fun listJoinedRooms(): Set<Address> {
        TODO("Not yet implemented")
    }

    override fun listHostedRooms(): Set<Address> {
        TODO("Not yet implemented")
    }

    override fun getChatInfo(chat: Address): Chat.Info {
        TODO("Not yet implemented")
    }
}
