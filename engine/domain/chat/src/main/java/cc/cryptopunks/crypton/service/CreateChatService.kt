package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.context.SessionCore
import cc.cryptopunks.crypton.context.createHandlers
import cc.cryptopunks.crypton.context.dispatch
import cc.cryptopunks.crypton.context.plus
import cc.cryptopunks.crypton.handler.handleCreateChat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CreateChatService(
    sessionCore: SessionCore
) : SessionCore by sessionCore,
    Connectable {

    private val handlers by lazy {
        createHandlers {
            with(session) {
                plus(handleCreateChat(navigate))
            }
        }
    }

    override val coroutineContext = SupervisorJob() + Dispatchers.IO

    override fun Connector.connect(): Job = session.scope.launch {
        input.collect { handlers.dispatch(it, output) }
    }
}
