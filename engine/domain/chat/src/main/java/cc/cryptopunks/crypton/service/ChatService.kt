package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.ChatCore
import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.context.Subscription
import cc.cryptopunks.crypton.context.dispatch
import cc.cryptopunks.crypton.context.createHandlers
import cc.cryptopunks.crypton.context.plus
import cc.cryptopunks.crypton.handler.handleCopy
import cc.cryptopunks.crypton.handler.handleGetMessages
import cc.cryptopunks.crypton.handler.handleLastMessageSubscription
import cc.cryptopunks.crypton.handler.handleMessageRead
import cc.cryptopunks.crypton.handler.handlePageMessagesSubscription
import cc.cryptopunks.crypton.handler.handlePopClipboard
import cc.cryptopunks.crypton.handler.handleSendMessage
import cc.cryptopunks.crypton.selector.MessagePagedListFlowSelector
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChatService(
    chatCore: ChatCore
) : ChatCore by chatCore,
    Connectable {

    private val log = typedLog()

    private val handlers by lazy {
        createHandlers {
            with(session) {
                plus(handleSendMessage(chat))
                plus(handleMessageRead())
                plus(handleLastMessageSubscription())
                plus(handlePopClipboard(clipboardStore))
                plus(handleCopy(clipboardSys))
                plus(handlePageMessagesSubscription(chat, messageFlow, log))
                plus(handleGetMessages())
            }
        }
    }

    private val messageFlow by lazy {
        MessagePagedListFlowSelector(
            repo = messageRepo,
            mainExecutor = mainExecutor,
            queryContext = queryContext
        )
    }

    override val coroutineContext = SupervisorJob() + Dispatchers.IO

    override fun Connector.connect(): Job = session.scope.launch {
        input.collect { handlers.dispatch(it, output) }
    }
}
