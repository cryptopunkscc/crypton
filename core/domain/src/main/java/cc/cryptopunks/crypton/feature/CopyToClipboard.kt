package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.clipboardSys
import cc.cryptopunks.crypton.factory.handler
import cc.cryptopunks.crypton.feature

internal fun copyToClipboard() = feature(

    handler = handler { _, (message): Exec.Copy ->
        clipboardSys.setClip(message.body)
    }
)
