package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import kotlinx.coroutines.flow.Flow

class MockChatNet(
    private val state: MockState
) : Chat.Net {

    override fun inviteToConference(chat: Address, users: List<Address>) {
        TODO("Not yet implemented")
    }

    override fun conferenceInvitationsFlow(): Flow<Chat.Net.ConferenceInvitation> {
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

    override fun joinConference(address: Address, nickname: String) {
        TODO("Not yet implemented")
    }

    override fun listJoinedRooms(): Set<Address> {
        TODO("Not yet implemented")
    }

    override fun listRooms(): Set<Address> {
        TODO("Not yet implemented")
    }
}
