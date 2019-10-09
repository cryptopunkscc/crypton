package cc.cryptopunks.crypton.smack.account

import cc.cryptopunks.crypton.entity.Account
import org.jivesoftware.smackx.iqregister.AccountManager

class RemoveAccount(
    accountManager: AccountManager
) : Account.Api.Remove, () -> Unit by {
    accountManager.deleteAccount()
}