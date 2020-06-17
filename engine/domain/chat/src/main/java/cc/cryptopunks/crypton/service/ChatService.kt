package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.ChatScope
import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.context.createHandlers
import cc.cryptopunks.crypton.context.dispatch
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChatService(
    chatScope: ChatScope
) : ChatScope by chatScope,
    Connectable {

    override val log = typedLog()

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

    override val coroutineContext get() = session.scope.coroutineContext

    override fun Connector.connect(): Job = launch {
        input.collect { handlers.dispatch(it, output) }
    }
}
