package cc.cryptopunks.crypton.smack.client

import cc.cryptopunks.crypton.api.Client
import org.jivesoftware.smackx.iqregister.AccountManager

class RemoveClient(
    accountManager: AccountManager
) : Client.Remove, () -> Unit by {
    accountManager.deleteAccount()
}