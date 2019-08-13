package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.xmpp.Xmpp
import cc.cryptopunks.crypton.xmpp.XmppQualifier
import cc.cryptopunks.crypton.xmpp.XmppScope
import cc.cryptopunks.crypton.xmpp.entities.ChatMessage
import cc.cryptopunks.crypton.xmpp.entities.Presence
import cc.cryptopunks.crypton.xmpp.entities.RosterEvent
import cc.cryptopunks.crypton.xmpp.entities.User
import cc.cryptopunks.crypton.smack.chat.ChatMessagePublisher
import cc.cryptopunks.crypton.smack.chat.ChatMessageSend
import cc.cryptopunks.crypton.smack.presence.PresenceSend
import cc.cryptopunks.crypton.smack.roster.RosterEventPublisher
import cc.cryptopunks.crypton.smack.user.UserGetContacts
import cc.cryptopunks.crypton.smack.user.UserInvite
import cc.cryptopunks.crypton.smack.user.UserInvited
import dagger.Binds
import dagger.Component
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import org.jivesoftware.smack.AbstractXMPPConnection
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.roster.Roster
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.iqregister.AccountManager
import org.jivesoftware.smackx.mam.MamManager
import org.jivesoftware.smackx.muc.MultiUserChatManager
import org.jivesoftware.smackx.offline.OfflineMessageManager
import org.jivesoftware.smackx.omemo.OmemoManager

@XmppScope
@Component(modules = [SmackComponent.Module::class])
internal interface SmackComponent : Xmpp {

    @get:XmppQualifier
    override val id: Long

    @dagger.Module(includes = [Bindings::class])
    class Module(
        @get:Provides @get:XmppQualifier val accountId: Long,
        @get:Provides val configuration: XMPPTCPConnectionConfiguration
    ) {

        @Provides
        @XmppScope
        fun user(connection: XMPPConnection) = User(
            jid = connection.user.jid()
        )

        @Provides
        @XmppScope
        fun tcpConnection() = XMPPTCPConnection(configuration).apply {
            connect()
        }

        @Provides
        @XmppScope
        fun accountManager(connection: XMPPConnection) = AccountManager.getInstance(connection)!!.apply {
            sensitiveOperationOverInsecureConnection(true)
        }

        @Provides
        @XmppScope
        fun mamManager(connection: XMPPConnection) = MamManager.getInstanceFor(connection)!!

        @Provides
        @XmppScope
        fun chatManager(connection: XMPPConnection) = ChatManager.getInstanceFor(connection)!!

        @Provides
        @XmppScope
        fun multiUserChatManager(connection: XMPPConnection) = MultiUserChatManager.getInstanceFor(connection)!!

        @Provides
        @XmppScope
        fun roster(connection: XMPPConnection) = Roster.getInstanceFor(connection)!!.apply {
            subscriptionMode = Roster.SubscriptionMode.reject_all
        }

        @Provides
        @XmppScope
        fun offlineMessageManager(connection: XMPPConnection) = OfflineMessageManager(connection)

        @Provides
        @XmppScope
        fun omemoManager(connection: XMPPConnection) = OmemoManager.getInstanceFor(connection)!!

        @Provides
        @XmppScope
        @XmppQualifier
        fun disposable() = CompositeDisposable()
    }

    @dagger.Module
    interface Bindings {

        @Binds
        @XmppScope
        fun abstractConnection(instance: XMPPTCPConnection): AbstractXMPPConnection

        @Binds
        @XmppScope
        fun connection(instance: AbstractXMPPConnection): XMPPConnection

        @Binds
        @XmppScope
        fun register(instance: XmppCreate): Xmpp.Create

        @Binds
        @XmppScope
        fun unregister(instance: XmppRemove): Xmpp.Remove

        @Binds
        @XmppScope
        fun connect(instance: XmppConnect): Xmpp.Connect

        @Binds
        @XmppScope
        fun disconnect(instance: XmppDisconnect): Xmpp.Disconnect

        @Binds
        @XmppScope
        fun login(instance: XmppLogin): Xmpp.Login

        @Binds
        @XmppScope
        fun isAuthenticated(instance: XmppIsAuthentificated): Xmpp.IsAuthenticated

        @Binds
        @XmppScope
        fun sendMessage(instance: ChatMessageSend): ChatMessage.Send

        @Binds
        @XmppScope
        fun sendPresence(instance: PresenceSend): Presence.Send

        @Binds
        @XmppScope
        fun chatMessagePublisher(instance: ChatMessagePublisher): ChatMessage.Publisher

        @Binds
        @XmppScope
        fun getUserContacts(instance: UserGetContacts): User.GetContacts

        @Binds
        @XmppScope
        fun inviteUser(instance: UserInvite): User.Invite

        @Binds
        @XmppScope
        fun invitedUser(instance: UserInvited): User.Invited

        @Binds
        @XmppScope
        fun rosterEventPublisher(instance: RosterEventPublisher): RosterEvent.Publisher
    }
}