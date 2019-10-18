package cc.cryptopunks.crypton.smack.module

import cc.cryptopunks.crypton.api.Api
import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.entity.*
import cc.cryptopunks.crypton.smack.api.account.*
import cc.cryptopunks.crypton.smack.api.chat.CreateChat
import cc.cryptopunks.crypton.smack.api.chat.MessageBroadcast
import cc.cryptopunks.crypton.smack.api.chat.ReadArchivedMessages
import cc.cryptopunks.crypton.smack.api.chat.SendMessage
import cc.cryptopunks.crypton.smack.api.client.ConnectClient
import cc.cryptopunks.crypton.smack.api.client.DisconnectClient
import cc.cryptopunks.crypton.smack.api.client.InitOmemo
import cc.cryptopunks.crypton.smack.api.presence.SendPresence
import cc.cryptopunks.crypton.smack.api.roster.RosterEventPublisher
import cc.cryptopunks.crypton.smack.api.user.AddContactUser
import cc.cryptopunks.crypton.smack.api.user.UserGetContacts
import cc.cryptopunks.crypton.smack.api.user.UserInvite
import cc.cryptopunks.crypton.smack.api.user.UserInvited
import cc.cryptopunks.crypton.smack.component.ApiComponent
import cc.cryptopunks.crypton.smack.component.SmackComponent
import cc.cryptopunks.crypton.smack.util.ConnectionEvent.ConnectionClosed
import cc.cryptopunks.crypton.smack.util.connectionEventsFlow
import cc.cryptopunks.crypton.util.BroadcastError
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter

internal class ApiModule(
    override val address: Address,
    broadcastError: BroadcastError,
    private val smack: SmackComponent
) : SmackComponent by smack,
    ApiComponent {

    override val apiScope = Api.Scope(broadcastError)

    init {
        apiScope.launch {
            smack.run {
                connection
                    .connectionEventsFlow()
                    .filter { it is ConnectionClosed && it.withError }
                    .collect {
                        connect()
                        login()
                    }
            }
        }
    }

    override val connect: Client.Connect by lazy {
        ConnectClient(connection = connection)
    }

    override val disconnect: Client.Disconnect by lazy {
        DisconnectClient(connection = connection)
    }

    override val isConnected: Client.IsConnected by lazy {
        IsAccountConnected(connection = connection)
    }

    override val initOmemo: Client.InitOmemo by lazy {
        InitOmemo(omemoManager)
    }

    override val createAccount: Account.Api.Create by lazy {
        CreateAccount(
            configuration = configuration,
            accountManager = accountManager
        )
    }

    override val remove: Account.Api.Remove by lazy {
        RemoveAccount(accountManager = accountManager)
    }
    override val login: Account.Api.Login by lazy {
        LoginAccount(connection)
    }

    override val isAuthenticated: Account.Api.IsAuthenticated by lazy {
        IsAccountAuthenticated(connection = connection)
    }

    override val getContacts: User.Api.GetContacts by lazy {
        UserGetContacts(roster = roster)
    }

    override val addContact: User.Api.AddContact by lazy {
        AddContactUser(roster = roster)
    }

    override val invite: User.Api.Invite by lazy {
        UserInvite(connection = connection)
    }

    override val invited: User.Api.Invited by lazy {
        UserInvited(connection = connection)
    }

    override val sendPresence: Presence.Api.Send by lazy {
        SendPresence(connection = connection)
    }

    override val sendMessage: Message.Api.Send by lazy {
        SendMessage(
            connection = connection,
            roster = roster,
            omemoManager = omemoManager
        )
    }

    override val messageBroadcast: Message.Api.Broadcast by lazy {
        MessageBroadcast(
            chatManager = chatManager,
            address = address,
            omemoManager = omemoManager
        )
    }

    override val readArchived: Message.Api.ReadArchived by lazy {
        ReadArchivedMessages(mamManager)
    }

    override val rosterEventPublisher: RosterEvent.Api.Broadcast by lazy {
        RosterEventPublisher(roster = roster)
    }

    override val createChat: Chat.Api.Create by lazy(::CreateChat)
}

private fun <T> SmackComponent.lazy(init: SmackComponent.() -> T) = kotlin.lazy { init() }
