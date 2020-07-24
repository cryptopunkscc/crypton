package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.handle

internal fun handleCopy() = handle { _, (message): Exec.Copy ->
    clipboardSys.setClip(message.text)
}
