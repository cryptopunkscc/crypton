package cc.cryptopunks.crypton.smack.api.client

import cc.cryptopunks.crypton.entity.Account
import org.jivesoftware.smackx.omemo.OmemoManager

class InitOmemo(
    omemoManager: OmemoManager
) : Account.Api.InitOmemo, () -> Unit by {
    runCatching {
        omemoManager.initialize()
    }
}