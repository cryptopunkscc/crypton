package cc.cryptopunks.crypton.execute

import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.Execute

internal val resolveAction: Execute = {
    copy(action = arg as Action)
}
