package cc.cryptopunks.crypton.smack.module

import cc.cryptopunks.crypton.api.Api
import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.entity.*
import cc.cryptopunks.crypton.smack.api.account.*
import cc.cryptopunks.crypton.smack.api.chat.MessageBroadcast
import cc.cryptopunks.crypton.smack.api.chat.SendMessage
import cc.cryptopunks.crypton.smack.api.client.ConnectClient
import cc.cryptopunks.crypton.smack.api.client.DisconnectClient
import cc.cryptopunks.crypton.smack.api.presence.SendPresence
import cc.cryptopunks.crypton.smack.api.roster.RosterEventPublisher
import cc.cryptopunks.crypton.smack.api.user.AddContactUser
import cc.cryptopunks.crypton.smack.api.user.UserGetContacts
import cc.cryptopunks.crypton.smack.api.user.UserInvite
import cc.cryptopunks.crypton.smack.api.user.UserInvited
import cc.cryptopunks.crypton.smack.component.ApiComponent
import cc.cryptopunks.crypton.smack.component.SmackComponent
import cc.cryptopunks.crypton.util.BroadcastError

internal class ApiModule(
    override val address: Address,
    broadcastError: BroadcastError,
    smackComponent: SmackComponent
) : SmackComponent by smackComponent,
    ApiComponent {

    override val apiScope: Api.Scope by lazy {
        Api.Scope(broadcastError)
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
        LoginAccount(connection = connection)
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
        SendMessage(connection = connection)
    }

    override val messageBroadcast: Message.Api.Broadcast by lazy {
        MessageBroadcast(
            chatManager = chatManager,
            address = address
        )
    }

    override val rosterEventPublisher: RosterEvent.Api.Broadcast by lazy {
        RosterEventPublisher(roster = roster)
    }

    override val createChat: Chat.Api.Create get() = TODO("not implemented")
}