package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.api.Client
import org.jivesoftware.smackx.iqregister.AccountManager
import javax.inject.Inject

@ApiScope
class RemoveClient @Inject constructor(
    accountManager: AccountManager
) : Client.Remove, () -> Unit by {
    accountManager.deleteAccount()
}