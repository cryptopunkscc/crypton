package cc.cryptopunks.crypton.execute

import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.Execute
import java.lang.IllegalArgumentException

internal val resolveAction: Execute = {
    try {
        copy(action = arg as Action)
    } catch (e: Throwable) {
        throw IllegalArgumentException(arg.toString(), e)
    }
}
