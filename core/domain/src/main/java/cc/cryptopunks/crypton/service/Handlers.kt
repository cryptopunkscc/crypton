package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.createHandlers
import cc.cryptopunks.crypton.handler.handleAccountListSubscription
import cc.cryptopunks.crypton.handler.handleAddAccount
import cc.cryptopunks.crypton.handler.handleClearInfoMessages
import cc.cryptopunks.crypton.handler.handleConferenceInvitations
import cc.cryptopunks.crypton.handler.handleCopy
import cc.cryptopunks.crypton.handler.handleCreateChat
import cc.cryptopunks.crypton.handler.handleDeleteChat
import cc.cryptopunks.crypton.handler.handleDeleteMessage
import cc.cryptopunks.crypton.handler.handleEnableAccount
import cc.cryptopunks.crypton.handler.handleEnqueueMessage
import cc.cryptopunks.crypton.handler.handleFlushMessageQueue
import cc.cryptopunks.crypton.handler.handleGetAccountList
import cc.cryptopunks.crypton.handler.handleGetMessages
import cc.cryptopunks.crypton.handler.handleGetPagedMessages
import cc.cryptopunks.crypton.handler.handleGetRosterItems
import cc.cryptopunks.crypton.handler.handleHasAccounts
import cc.cryptopunks.crypton.handler.handleInvite
import cc.cryptopunks.crypton.handler.handleJoin
import cc.cryptopunks.crypton.handler.handleLastMessageSubscription
import cc.cryptopunks.crypton.handler.handleListJoinedRooms
import cc.cryptopunks.crypton.handler.handleListRooms
import cc.cryptopunks.crypton.handler.handleLogin
import cc.cryptopunks.crypton.handler.handleLogout
import cc.cryptopunks.crypton.handler.handleMessageRead
import cc.cryptopunks.crypton.handler.handlePageMessagesSubscription
import cc.cryptopunks.crypton.handler.handlePopClipboard
import cc.cryptopunks.crypton.handler.handlePresenceChanged
import cc.cryptopunks.crypton.handler.handleRegisterAccount
import cc.cryptopunks.crypton.handler.handleRemoveAccount
import cc.cryptopunks.crypton.handler.handleRosterItemsSubscription
import cc.cryptopunks.crypton.handler.handleSaveInfoMessage
import cc.cryptopunks.crypton.handler.handleSaveMessages
import cc.cryptopunks.crypton.handler.handleSessionAction
import cc.cryptopunks.crypton.handler.handleStartServices
import cc.cryptopunks.crypton.handler.handleSubscriptionAccept
import cc.cryptopunks.crypton.handler.handleUpdateChatNotification
import handleAccountAuthenticated
import handleConfigureChat
import handleGetChatInfo


fun cryptonHandlers() = createHandlers {
    +appServiceHandlers()
    +sessionServiceHandlers()
    +accountHandlers()
    +rosterHandlers()
    +chatHandlers()
}

fun appServiceHandlers() = createHandlers {
    +handleHasAccounts()
    +handleSessionAction()
    +handleStartServices()
}

fun sessionServiceHandlers() = createHandlers {
    +handleSaveMessages()
    +handlePresenceChanged()
    +handleFlushMessageQueue()
    +handleConferenceInvitations()
    +handleUpdateChatNotification()
    +handleAccountAuthenticated()
    +handleJoin()
}


fun accountHandlers() = createHandlers {
    +handleRegisterAccount()
    +handleAddAccount()
    +handleLogin()
    +handleLogout()
    +handleEnableAccount()
    +handleRemoveAccount()
    +handleGetAccountList()
    +handleAccountListSubscription()
}

fun rosterHandlers() = createHandlers {
    +handleGetRosterItems()
    +handleRosterItemsSubscription()
    +handleSubscriptionAccept()
    +handleListRooms()
    +handleListJoinedRooms()
}

fun chatHandlers() = createHandlers {
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
    +handleCreateChat()
    +handleGetChatInfo()
}
