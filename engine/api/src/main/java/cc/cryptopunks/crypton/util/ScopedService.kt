package cc.cryptopunks.crypton.util

import cc.cryptopunks.crypton.context.BaseScope
import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.context.HandlerRegistry
import cc.cryptopunks.crypton.context.dispatch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


fun <T: BaseScope> service(
    scope: T,
    createRegistry: T.() -> HandlerRegistry
) : Connectable =
    ScopedService(scope, scope.createRegistry())


private data class ScopedService(
    val scope: BaseScope,
    val handlers: HandlerRegistry = emptyMap()
) :
    Connectable,
    CoroutineScope by scope {

    override fun Connector.connect(): Job = launch {
        scope.log.d("Start $id")
        input.collect {
            scope.log.d("$id $it")
            handlers.dispatch(it, output)?.join()
        }
    }
}
