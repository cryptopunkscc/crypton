package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.createHandlers
import cc.cryptopunks.crypton.handler.handleAccountsSubscription
import cc.cryptopunks.crypton.handler.handleAddAccount
import cc.cryptopunks.crypton.handler.handleClearInfoMessages
import cc.cryptopunks.crypton.handler.handleConfigureChat
import cc.cryptopunks.crypton.handler.handleCopy
import cc.cryptopunks.crypton.handler.handleCreateChat
import cc.cryptopunks.crypton.handler.handleDeleteChat
import cc.cryptopunks.crypton.handler.handleDeleteMessage
import cc.cryptopunks.crypton.handler.handleEnableAccount
import cc.cryptopunks.crypton.handler.handleEnqueueMessage
import cc.cryptopunks.crypton.handler.handleFlushMessageQueue
import cc.cryptopunks.crypton.handler.handleGetAccountList
import cc.cryptopunks.crypton.handler.handleGetChatInfo
import cc.cryptopunks.crypton.handler.handleGetJoinedRooms
import cc.cryptopunks.crypton.handler.handleGetMessages
import cc.cryptopunks.crypton.handler.handleGetPagedMessages
import cc.cryptopunks.crypton.handler.handleGetHostedRooms
import cc.cryptopunks.crypton.handler.handleGetRosterItems
import cc.cryptopunks.crypton.handler.handleGetSubscriptionStatus
import cc.cryptopunks.crypton.handler.handleInsertInvitation
import cc.cryptopunks.crypton.handler.handleInvite
import cc.cryptopunks.crypton.handler.handleJoinChat
import cc.cryptopunks.crypton.handler.handleLastMessageSubscription
import cc.cryptopunks.crypton.handler.handleListAccounts
import cc.cryptopunks.crypton.handler.handleLogin
import cc.cryptopunks.crypton.handler.handleLogout
import cc.cryptopunks.crypton.handler.handleMessageRead
import cc.cryptopunks.crypton.handler.handlePageMessagesSubscription
import cc.cryptopunks.crypton.handler.handlePopClipboard
import cc.cryptopunks.crypton.handler.handlePresence
import cc.cryptopunks.crypton.handler.handlePurgeDeviceList
import cc.cryptopunks.crypton.handler.handleReconnectSession
import cc.cryptopunks.crypton.handler.handleRegisterAccount
import cc.cryptopunks.crypton.handler.handleRemoveAccount
import cc.cryptopunks.crypton.handler.handleRosterItemsSubscription
import cc.cryptopunks.crypton.handler.handleSaveInfoMessage
import cc.cryptopunks.crypton.handler.handleSaveMessages
import cc.cryptopunks.crypton.handler.handleSessionAction
import cc.cryptopunks.crypton.handler.handleStartSessionService
import cc.cryptopunks.crypton.handler.handleSyncConferences
import cc.cryptopunks.crypton.handler.handleToggleIndicator
import cc.cryptopunks.crypton.handler.handleUpdateChatNotification

fun cryptonHandlers() = createHandlers {
    +appServiceHandlers()
    +sessionServiceHandlers()
    +mainHandlers()
    +accountHandlers()
    +chatHandlers()
}

fun appServiceHandlers() = createHandlers {
    +handleSessionAction()
    +handleToggleIndicator()
    +handleStartSessionService()
}

fun sessionServiceHandlers() = createHandlers {
    +handleJoinChat()
    +handlePresence()
    +handleSaveMessages()
    +handleSyncConferences()
    +handleInsertInvitation()
    +handleFlushMessageQueue()
    +handleUpdateChatNotification()
    +handleReconnectSession()
}

fun mainHandlers() = createHandlers {
    +handleListAccounts()
    +handleCopy()
    +handleLogin()
    +handleAddAccount()
    +handlePopClipboard()
    +handleGetRosterItems()
    +handleRegisterAccount()
    +handleRosterItemsSubscription()
}

fun accountHandlers() = createHandlers {
    +handleLogout()
    +handleGetHostedRooms()
    +handleCreateChat()
    +handleMessageRead()
    +handleDeleteMessage()
    +handleEnableAccount()
    +handleRemoveAccount()
    +handleGetAccountList()
    +handleGetJoinedRooms()
    +handleAccountsSubscription()
    +handlePurgeDeviceList()
    +handleGetSubscriptionStatus()
}

fun chatHandlers() = createHandlers {
    +handleInvite()
    +handleDeleteChat()
    +handleGetMessages()
    +handleGetChatInfo()
    +handleConfigureChat()
    +handleEnqueueMessage()
    +handleSaveInfoMessage()
    +handleGetPagedMessages()
    +handleClearInfoMessages()
    +handleLastMessageSubscription()
    +handlePageMessagesSubscription()
}
