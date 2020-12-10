package cc.cryptopunks.crypton.smack.net.roster

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.smack.SmackCore
import cc.cryptopunks.crypton.smack.util.SmackPresence
import cc.cryptopunks.crypton.smack.util.address
import cc.cryptopunks.crypton.smack.util.entityBareJid
import cc.cryptopunks.crypton.smack.util.presence
import kotlinx.coroutines.flow.Flow
import org.jxmpp.jid.impl.JidCreate

internal class RosterNet(
    smack: SmackCore
) : SmackCore by smack,
    Roster.Net {
    override fun getContacts(): List<Address> = roster.entries.map { entry ->
        entry.jid.address()
    }

    override fun addContact(user: Address) {
        roster.createEntry(
            user.entityBareJid(),
            user.local,
            null
        )
    }

    override fun invite(address: Address) {
        connection.sendStanza(
            SmackPresence(
                JidCreate.from(address.toString()),
                org.jivesoftware.smack.packet.Presence.Type.subscribe
            )
        )
    }

    override fun invited(address: Address) {
        connection.sendStanza(
            SmackPresence(
                JidCreate.from(address.toString()),
                org.jivesoftware.smack.packet.Presence.Type.subscribed
            )
        )
    }

    override fun subscriptionStatus(address: Address) = roster.getEntry(address.entityBareJid())?.run {
        Roster.Item.Status.valueOf(type.name)
    } ?: Roster.Item.Status.none

    override fun subscribe(address: Address) =
        roster.createEntry(address.entityBareJid(), address.local, emptyArray())

    override fun iAmSubscribed(address: Address) = roster.iAmSubscribedTo(address.entityBareJid())

    override fun sendPresence(presence: Presence) {
        connection.sendStanza(
            SmackPresence(
                org.jivesoftware.smack.packet.Presence.Type.fromString(
                    presence.status.name.toLowerCase()
                )
            ).apply {
                when (presence.status) {
                    Presence.Status.Subscribed,
                    Presence.Status.Subscribe
                    -> to = presence.resource.address.entityBareJid()
                }
            }
        )
    }


    override fun getCachedPresences(): List<Presence> = roster.run {
        entries.map { entry -> getPresence(entry.jid).presence(entry.jid) }
    }

    override val rosterEvents: Flow<Roster.Event> get() = roster.rosterEventFlow()
}
