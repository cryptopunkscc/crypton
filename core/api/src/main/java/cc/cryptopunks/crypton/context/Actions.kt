package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.Async
import cc.cryptopunks.crypton.Subscription

object Exec {

    // Internal

    data class ToggleIndicator(val show: Boolean) : Main.Action
    object SyncConferences : Account.Action, Async
    data class UpdateNotification(val messages: List<Message>) : Account.Action
    data class HandlePresence(val presence: Presence) : Account.Action {
        override val channelId: Int get() = 10
    }
    data class SaveMessages(val messages: List<Message>) : Account.Action
    data class FlushQueuedMessages(val addresses: Set<Address> = emptySet()) : Account.Action
    data class InsertInvitation(val address: Address, val inviter: Resource) : Account.Action

    // Main

    interface Authenticate : Main.Action
    data class Register(val account: Account) : Authenticate
    data class Login(val account: Account) : Authenticate
    object PopClipboard : Main.Action
    data class Copy(val message: Message) : Main.Action

    // Account

    object Connect : Account.Action
    object Disconnect : Account.Action
    object PurgeDeviceList : Account.Action
    data class EnableAccount(val condition: Boolean) : Account.Action
    data class RemoveAccount(val deviceOnly: Boolean = true) : Account.Action
    data class MessagesRead(val messages: List<Message>) : Account.Action
    data class DeleteMessage(val message: Message) : Account.Action
    data class CreateChat(val chat: Chat) : Account.Action
    data class DownloadFromMessage(val messageId: String) : Account.Action

    // Chat

    data class EnqueueMessage(val text: String, val encrypted: Boolean = true) : Chat.Action
    data class Invite(val users: Set<Address>) : Chat.Action
    object ClearInfoMessages : Chat.Action
    object DeleteChat : Chat.Action
    class ConfigureConference : Chat.Action
    data class SaveInfoMessage(val text: String) : Chat.Action // TODO
    object JoinChat : Chat.Action
    object Select {
        data class Item(val item: Roster.Item) : Chat.Action
        enum class Media: Chat.Action { File, Image, Video, Photo, Location }
        object File : Chat.Action
        object Image : Chat.Action
        object Video : Chat.Action
        object Photo : Chat.Action
        object Location : Chat.Action
    }

    data class Upload(val uri: URI) : Chat.Action
}

object Get {

    // Main

    object AccountNames : Main.Action
    object Accounts : Main.Action
    object RosterItems : Main.Action

    // Account

    object HostedRooms : Account.Action
    object JoinedRooms : Account.Action
    data class SubscriptionStatus(val address: Address): Account.Action

    // Chat

    object PagedMessages : Chat.Action
    object Messages : Chat.Action
    object ChatInfo : Chat.Action
}

object Subscribe {

    // Internal

    object AppService : Main.Action, Subscription
    object SessionService : Account.Action, Async

    // Main

    data class Accounts(override val enable: Boolean) : Main.Action, Subscription
    data class RosterItems(
        override val enable: Boolean,
        val account: Address? = null,
        val list: Boolean = true
    ) : Main.Action, Subscription

    // Account

    object ReconnectSession : Account.Action, Subscription

    // Chat

    data class PagedMessages(override val enable: Boolean) : Chat.Action, Subscription
    data class LastMessage(override val enable: Boolean) : Chat.Action, Subscription
    data class OnMessageExecute(val command: String, override val enable: Boolean = true) : Chat.Action, Subscription
}
