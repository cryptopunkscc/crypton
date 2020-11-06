package cc.cryptopunks.crypton.context

import androidx.paging.DataSource
import androidx.paging.PagedList
import cc.cryptopunks.crypton.Scoped
import kotlinx.coroutines.flow.Flow

data class Chat(
    val address: Address = Address.Empty,
    val account: Address = Address.Empty,
    val users: List<Address> = emptyList(),
    val title: String = ""
) {
    val isConference = address.isConference

    interface Action : Scoped<ChatScope>

    companion object {
        val Empty = Chat()
    }

    data class Member(
        val nick: String?,
        val address: Address,
        val role: Role,
        val affiliation: Affiliation
    )

    enum class Role { moderator, none, participant, visitor, unknown }

    enum class Affiliation { owner, admin, member, outcast, none, unknown }

    // Events

    data class MessageText(val text: CharSequence?)

    data class PagedMessages(val account: Address, val list: PagedList<Message>)

    data class Messages(val account: Address, val list: List<Message>)

    interface Rooms {
        val set: Set<Address>
    }

    data class JoinedRooms(override val set: Set<Address>) : Rooms

    data class AllRooms(override val set: Set<Address>) : Rooms

    data class Info(
        val name: String,
        val account: Address,
        val address: Address,
        val members: Set<Member> = emptySet()
    )

    data class Invitation(
        val address: Address,
        val inviter: Resource,
        val reason: String?,
        val password: String?
    )

    interface Net {
        fun supportEncryption(address: Address): Boolean
        fun createOrJoinConference(chat: Chat): Chat
        fun configureConference(chat: Address)
        fun inviteToConference(chat: Address, users: Set<Address>)
        fun conferenceInvitationsFlow(): Flow<Invitation>
        fun joinConference(address: Address, nickname: String, historySince: Long = 0)
        fun listJoinedRooms(): Set<Address>
        fun listHostedRooms(): Set<Address>
        fun getChatInfo(chat: Address): Info
    }


    interface Repo {
        suspend fun get(address: Address): Chat
        suspend fun contains(address: Address): Boolean
        suspend fun list(): List<Chat>
        suspend fun insert(chat: Chat)
        suspend fun insertIfNeeded(chat: Chat)
        suspend fun delete(chats: List<Address>)
        suspend fun deleteAll()
        fun dataSourceFactory(): DataSource.Factory<Int, Chat>
        fun flowList(): Flow<List<Chat>>
    }
}

fun Chat.validate() = apply {
    address.validate()
}
