package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.xmpp.Xmpp
import cc.cryptopunks.crypton.xmpp.XmppScope
import org.jivesoftware.smackx.iqregister.AccountManager
import javax.inject.Inject

@XmppScope
class XmppRemove @Inject constructor(
    accountManager: AccountManager
) : Xmpp.Remove, () -> Unit by {
    accountManager.deleteAccount()
}