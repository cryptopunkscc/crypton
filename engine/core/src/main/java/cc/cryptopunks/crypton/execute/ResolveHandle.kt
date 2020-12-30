package cc.cryptopunks.crypton.execute

import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.Execute
import cc.cryptopunks.crypton.Handle
import cc.cryptopunks.crypton.Handler
import cc.cryptopunks.crypton.type
import cc.cryptopunks.crypton.util.mapNotNull
import kotlinx.coroutines.CoroutineScope

val resolveHandle: Execute = {
    copy(handle = scope.handleFor(action))
}


internal fun CoroutineScope.handleFor(action: Action): Handle<Action> =
    requireNotNull(getHandler(action)) {
        """
Cannot find handler for $action
Available handlers: 
${coroutineContext.mapNotNull { it is Handler<*> }.joinToString("\n")}
        """.trimIndent()
    }.handle


private fun CoroutineScope.getHandler(action: Action): Handler<Action>? =
    coroutineContext[Handler.Key(action.type)]
