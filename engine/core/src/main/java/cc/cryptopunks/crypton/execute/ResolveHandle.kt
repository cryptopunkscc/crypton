package cc.cryptopunks.crypton.execute

import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.Execute
import cc.cryptopunks.crypton.Handle
import cc.cryptopunks.crypton.Handler
import cc.cryptopunks.crypton.type
import kotlinx.coroutines.CoroutineScope

val resolveHandle: Execute = {
    copy(handle = scope.handleFor(action))
}


internal fun CoroutineScope.handleFor(action: Action): Handle<Action> =
    requireNotNull(getHandler(action)) {
        "Cannot find handler for $action"
    }.handle


private fun CoroutineScope.getHandler(action: Action): Handler<Action>? =
    coroutineContext[Handler.Key(action.type)]
