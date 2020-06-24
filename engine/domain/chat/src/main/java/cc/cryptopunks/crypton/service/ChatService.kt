package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.ChatScope
import cc.cryptopunks.crypton.context.createHandlers
import cc.cryptopunks.crypton.handler.handleCopy
import cc.cryptopunks.crypton.handler.handleCreateChat
import cc.cryptopunks.crypton.handler.handleEnqueueMessage
import cc.cryptopunks.crypton.handler.handleGetMessages
import cc.cryptopunks.crypton.handler.handleLastMessageSubscription
import cc.cryptopunks.crypton.handler.handleMessageRead
import cc.cryptopunks.crypton.handler.handlePageMessagesSubscription
import cc.cryptopunks.crypton.handler.handlePopClipboard
import cc.cryptopunks.crypton.util.service

fun chatService(scope: ChatScope) = service(scope) {
    chatHandlers()
}

fun ChatScope.chatHandlers() = createHandlers {
    +handleEnqueueMessage()
    +handleMessageRead()
    +handleLastMessageSubscription()
    +handlePopClipboard()
    +handleCopy()
    +handlePageMessagesSubscription()
    +handleGetMessages()
}

fun createChatService(scope: AppScope) = service(scope) {
    createChatHandlers()
}

fun AppScope.createChatHandlers() = createHandlers {
    +handleCreateChat()
}

