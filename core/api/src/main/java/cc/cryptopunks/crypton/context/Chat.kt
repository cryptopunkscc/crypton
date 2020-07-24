package cc.cryptopunks.crypton.context

import androidx.paging.DataSource
import androidx.paging.PagedList
import cc.cryptopunks.crypton.Scoped
import cc.cryptopunks.crypton.Subscription
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

    object Service {

        // command

        object PopClipboard : Main.Action

        data class MessagesRead(val messages: List<Message>) : Account.Action

        data class EnqueueMessage(val text: String, val encrypted: Boolean = true) : Action

        data class FlushQueuedMessages(val addresses: Set<Address> = emptySet()) : Account.Action

        object ClearInfoMessages : Action

        data class UpdateNotification(val messages: List<Message>) : Account.Action

        data class Copy(val message: Message) : Account.Action

        data class Delete(val message: Message) : Account.Action

        data class Create(val chat: Chat) : Account.Action

        data class Invite(val users: Set<Address>) : Action

        object DeleteChat : Action

        class Configure : Action

        data class InfoMessage(val text: String) : Action

        // Query

        object GetPagedMessages : Action

        object GetMessages : Action

        object ListJoinedRooms : Account.Action

        object ListRooms : Account.Action

        object GetInfo : Action

        // Subscribe

        data class SubscribePagedMessages(override val enable: Boolean) : Action, Subscription

        data class SubscribeLastMessage(override val enable: Boolean) : Action, Subscription

        data class SubscribeListMessages(
            override var enable: Boolean = true,
            val from: Long = 0,
            val to: Long = System.currentTimeMillis()
        ) : Action, Subscription

        // Event

        data class ChatCreated(val chat: Address)

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
    }


    interface Net {
        fun supportEncryption(address: Address): Boolean
        fun createOrJoinConference(chat: Chat): Chat
        fun configureConference(chat: Address)
        fun inviteToConference(chat: Address, users: Set<Address>)
        fun conferenceInvitationsFlow(): Flow<ConferenceInvitation>
        fun joinConference(address: Address, nickname: String, historySince: Int = 0)
        fun listJoinedRooms(): Set<Address>
        fun listRooms(): Set<Address>
        fun getChatInfo(chat: Address): Service.Info

        interface Event : Api.Event
        data class Joined(val chat: Chat) : Event

        interface EventFlow : Flow<Event>

        data class ConferenceInvitation(
            val address: Address,
            val inviter: Resource,
            val reason: String?,
            val password: String?
        ) : Action
    }


    interface Repo {
        suspend fun get(address: Address): Chat
        suspend fun contains(address: Address): Boolean
        suspend fun list(): List<Chat>
        suspend fun list(addresses: List<Address>): List<Chat>
        suspend fun insert(chat: Chat)
        suspend fun insertIfNeeded(chat: Chat)
        suspend fun delete(chats: List<Address>)
        suspend fun deleteAll()
        fun dataSourceFactory(): DataSource.Factory<Int, Chat>
        fun flowList(): Flow<List<Chat>>
    }
}

suspend fun SessionScope.createChat(chat: Chat) {
    log.d("Creating $chat")
    if (chat.isConference && chat.address !in listJoinedRooms()) createOrJoinConference(chat)
    log.d("Chat ${chat.address} with users ${chat.users} created")
    insertChat(chat)
}

suspend fun SessionScope.insertChat(chat: Chat) {
    log.d("Inserting $chat")
    chatRepo.insertIfNeeded(chat)
    log.d("Chat ${chat.address} with users ${chat.users} Inserted")
}
