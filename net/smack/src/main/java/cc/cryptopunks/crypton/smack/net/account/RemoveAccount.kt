package cc.cryptopunks.crypton.smack.net.account

import cc.cryptopunks.crypton.entity.Account
import org.jivesoftware.smackx.iqregister.AccountManager

class RemoveAccount(
    accountManager: AccountManager
) : Account.Net.Remove, () -> Unit by {
    accountManager.deleteAccount()
}