package cc.cryptopunks.crypton.smack.api.client

import cc.cryptopunks.crypton.api.Client
import org.jivesoftware.smackx.omemo.OmemoManager

class InitOmemo(
    omemoManager: OmemoManager
) : Client.InitOmemo, () -> Unit by {
    runCatching {
        // It always throws timeout exception, but works anyway for some reason
        // so i left it in caching block. Maybe will be fixed in further version.
        omemoManager.initialize()
    }
}