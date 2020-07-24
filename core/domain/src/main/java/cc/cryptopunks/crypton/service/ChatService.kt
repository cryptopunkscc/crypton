package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.createHandlers
import cc.cryptopunks.crypton.handler.handleClearInfoMessages
import cc.cryptopunks.crypton.handler.handleCopy
import cc.cryptopunks.crypton.handler.handleCreateChat
import cc.cryptopunks.crypton.handler.handleDeleteChat
import cc.cryptopunks.crypton.handler.handleDeleteMessage
import cc.cryptopunks.crypton.handler.handleEnqueueMessage
import cc.cryptopunks.crypton.handler.handleGetMessages
import cc.cryptopunks.crypton.handler.handleGetPagedMessages
import cc.cryptopunks.crypton.handler.handleInvite
import cc.cryptopunks.crypton.handler.handleLastMessageSubscription
import cc.cryptopunks.crypton.handler.handleMessageRead
import cc.cryptopunks.crypton.handler.handlePageMessagesSubscription
import cc.cryptopunks.crypton.handler.handlePopClipboard
import cc.cryptopunks.crypton.handler.handleSaveInfoMessage
import handleConfigureChat
import handleGetChatInfo

val chatHandlers get() = createHandlers {
    +handleEnqueueMessage()
    +handleMessageRead()
    +handleLastMessageSubscription()
    +handlePopClipboard()
    +handleCopy()
    +handlePageMessagesSubscription()
    +handleGetPagedMessages()
    +handleGetMessages()
    +handleInvite()
    +handleSaveInfoMessage()
    +handleClearInfoMessages()
    +handleDeleteMessage()
    +handleDeleteChat()
    +handleGetChatInfo()
    +handleConfigureChat()
}

val appChatHandlers get() = createHandlers {
    +handleCreateChat()
    +handleGetChatInfo()
}
