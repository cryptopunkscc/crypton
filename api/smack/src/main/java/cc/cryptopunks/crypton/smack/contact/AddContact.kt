package cc.cryptopunks.crypton.smack.contact

import cc.cryptopunks.crypton.api.ApiScope
import cc.cryptopunks.crypton.entity.Contact
import org.jivesoftware.smack.roster.Roster
import org.jxmpp.jid.impl.JidCreate
import javax.inject.Inject

@ApiScope
class AddContact @Inject constructor(
    private val roster: Roster
) : Contact.Add {

    override fun invoke(contact: Contact) {
        roster.createEntry(
            JidCreate.bareFrom(contact.remoteId),
            contact.name,
            arrayOf()
        )
    }
}