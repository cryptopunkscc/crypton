package cc.cryptopunks.crypton.smack.module

import cc.cryptopunks.crypton.entity.*
import cc.cryptopunks.crypton.net.Net
import cc.cryptopunks.crypton.smack.component.SmackComponent
import cc.cryptopunks.crypton.smack.net.NetEventOutput
import cc.cryptopunks.crypton.smack.net.account.*
import cc.cryptopunks.crypton.smack.net.chat.*
import cc.cryptopunks.crypton.smack.net.client.ConnectClient
import cc.cryptopunks.crypton.smack.net.client.DisconnectClient
import cc.cryptopunks.crypton.smack.net.client.InitOmemo
import cc.cryptopunks.crypton.smack.net.presence.GetCachedPresences
import cc.cryptopunks.crypton.smack.net.presence.SendPresence
import cc.cryptopunks.crypton.smack.net.roster.RosterEventBroadcast
import cc.cryptopunks.crypton.smack.net.user.AddContactUser
import cc.cryptopunks.crypton.smack.net.user.UserGetContacts
import cc.cryptopunks.crypton.smack.net.user.UserInvite
import cc.cryptopunks.crypton.smack.net.user.UserInvited
import cc.cryptopunks.crypton.util.BroadcastErrorScope

internal class NetModule(
    scope: BroadcastErrorScope,
    private val address: Address,
    private val smack: SmackComponent
) : SmackComponent by smack,
    Net {

    private val encryptedMessageCache by lazy { EncryptedMessageCache() }

    override val netEvents: Net.Event.Output by lazy {
        NetEventOutput(connection = connection)
    }

    override val connect: Account.Net.Connect by lazy {
        ConnectClient(connection = connection)
    }

    override val disconnect: Account.Net.Disconnect by lazy {
        DisconnectClient(connection = connection)
    }

    override val isConnected: Account.Net.IsConnected by lazy {
        IsAccountConnected(connection = connection)
    }

    override val initOmemo: Account.Net.InitOmemo by lazy {
        InitOmemo(omemoManager)
    }

    override val createAccount: Account.Net.Create by lazy {
        CreateAccount(
            configuration = configuration,
            accountManager = accountManager
        )
    }

    override val remove: Account.Net.Remove by lazy {
        RemoveAccount(accountManager = accountManager)
    }
    override val login: Account.Net.Login by lazy {
        LoginAccount(
            connection = connection,
            carbonManager = carbonManager
        )
    }

    override val isAuthenticated: Account.Net.IsAuthenticated by lazy {
        IsAccountAuthenticated(connection = connection)
    }

    override val statusFlow: Account.Net.StatusFlow by lazy {
        AccountStatusFlow(connection)
    }

    override val getContacts: User.Net.GetContacts by lazy {
        UserGetContacts(roster = roster)
    }

    override val addContact: User.Net.AddContact by lazy {
        AddContactUser(roster = roster)
    }

    override val invite: User.Net.Invite by lazy {
        UserInvite(connection = connection)
    }

    override val invited: User.Net.Invited by lazy {
        UserInvited(connection = connection)
    }

    override val sendPresence: Presence.Net.Send by lazy {
        SendPresence(connection = connection)
    }

    override val sendMessage: Message.Net.Send by lazy {
        SendMessage(
            connection = connection,
            roster = roster,
            omemoManager = omemoManager,
            encryptedMessageCache = encryptedMessageCache
        )
    }

    override val messageBroadcast: Message.Net.Broadcast by lazy {
        MessageBroadcast(
            scope = scope,
            chatManager = chatManager,
            address = address,
            omemoManager = omemoManager,
            encryptedMessageCache = encryptedMessageCache
        )
    }

    override val readArchived: Message.Net.ReadArchived by lazy {
        ReadArchivedMessages(
            mamManager = mamManager,
            omemoManager = omemoManager
        )
    }

    override val rosterEventPublisher: RosterEvent.Net.Broadcast by lazy {
        RosterEventBroadcast(roster = roster)
    }

    override val createChat: Chat.Net.Create by lazy(::CreateChat)

    override val getCached: UserPresence.Net.GetCached by lazy {
        GetCachedPresences(roster)
    }
}

private fun <T> SmackComponent.lazy(init: SmackComponent.() -> T) = kotlin.lazy { init() }
