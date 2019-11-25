package cc.cryptopunks.crypton.smack.net.client

import cc.cryptopunks.crypton.net.Net
import org.jivesoftware.smackx.omemo.OmemoManager

class InitOmemo(
    omemoManager: OmemoManager
) : Net.InitOmemo, () -> Unit by {
    runCatching {
        omemoManager.initialize()
    }
}