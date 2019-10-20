package cc.cryptopunks.crypton.smack.net.client

import cc.cryptopunks.crypton.entity.Account
import org.jivesoftware.smackx.omemo.OmemoManager

class InitOmemo(
    omemoManager: OmemoManager
) : Account.Net.InitOmemo, () -> Unit by {
    runCatching {
        omemoManager.initialize()
    }
}