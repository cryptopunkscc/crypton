package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.create.cryptonContext
import cc.cryptopunks.crypton.feature.addAccount
import cc.cryptopunks.crypton.feature.clearInfoMessages
import cc.cryptopunks.crypton.feature.cliConfigure
import cc.cryptopunks.crypton.feature.cliSetAccount
import cc.cryptopunks.crypton.feature.cliSetChat
import cc.cryptopunks.crypton.feature.configureChat
import cc.cryptopunks.crypton.feature.connect
import cc.cryptopunks.crypton.feature.copyToClipboard
import cc.cryptopunks.crypton.feature.createChat
import cc.cryptopunks.crypton.feature.deleteChat
import cc.cryptopunks.crypton.feature.deleteMessage
import cc.cryptopunks.crypton.feature.downloadFile
import cc.cryptopunks.crypton.feature.enableAccount
import cc.cryptopunks.crypton.feature.enqueueMessage
import cc.cryptopunks.crypton.feature.flushMessageQueue
import cc.cryptopunks.crypton.feature.getAccountNames
import cc.cryptopunks.crypton.feature.getAccounts
import cc.cryptopunks.crypton.feature.getChatInfo
import cc.cryptopunks.crypton.feature.getHostedRooms
import cc.cryptopunks.crypton.feature.getJoinedRooms
import cc.cryptopunks.crypton.feature.getMessages
import cc.cryptopunks.crypton.feature.getPagedMessages
import cc.cryptopunks.crypton.feature.getRosterItems
import cc.cryptopunks.crypton.feature.getSubscriptionStatus
import cc.cryptopunks.crypton.feature.handlePresence
import cc.cryptopunks.crypton.feature.insertInvitation
import cc.cryptopunks.crypton.feature.inviteToConference
import cc.cryptopunks.crypton.feature.joinChat
import cc.cryptopunks.crypton.feature.logout
import cc.cryptopunks.crypton.feature.messageRead
import cc.cryptopunks.crypton.feature.popClipboard
import cc.cryptopunks.crypton.feature.purgeDeviceList
import cc.cryptopunks.crypton.feature.reconnectSession
import cc.cryptopunks.crypton.feature.registerAccount
import cc.cryptopunks.crypton.feature.removeAccount
import cc.cryptopunks.crypton.feature.saveInfoMessage
import cc.cryptopunks.crypton.feature.saveMessages
import cc.cryptopunks.crypton.feature.startAppServices
import cc.cryptopunks.crypton.feature.startSessionService
import cc.cryptopunks.crypton.feature.subscribeAccounts
import cc.cryptopunks.crypton.feature.subscribeLastMessage
import cc.cryptopunks.crypton.feature.subscribeOnMessageExecute
import cc.cryptopunks.crypton.feature.subscribePagedMessages
import cc.cryptopunks.crypton.feature.subscribeRosterItems
import cc.cryptopunks.crypton.feature.syncConferences
import cc.cryptopunks.crypton.feature.toggleIndicator
import cc.cryptopunks.crypton.feature.updateChatNotification
import cc.cryptopunks.crypton.feature.uploadFile
import cc.cryptopunks.crypton.resolvers.scopedResolver
import kotlin.coroutines.CoroutineContext

fun cryptonFeatures() = cryptonContext(
    // cli
    cliConfigure(),
    cliSetAccount(),
    cliSetChat(),

    // main
    startAppServices(),
    toggleIndicator(),
    addAccount(),
    registerAccount(),
    getAccountNames(),
    subscribeAccounts(),
    getAccounts(),
    enableAccount(),
    getRosterItems(),
    subscribeRosterItems(),
    popClipboard(),

    // account
    startSessionService(),
    connect(),
    logout(),
    removeAccount(),
    reconnectSession(),
    handlePresence(),
    insertInvitation(),
    purgeDeviceList(),
    createChat(),
    joinChat(),
    getHostedRooms(),
    getJoinedRooms(),
    getSubscriptionStatus(),
    messageRead(),
    flushMessageQueue(),
    saveMessages(),

    // chat
    clearInfoMessages(),
    copyToClipboard(),
    getChatInfo(),
    deleteChat(),
    configureChat(),
    syncConferences(),
    updateChatNotification(),
    inviteToConference(),
    saveInfoMessage(),
    enqueueMessage(),
    deleteMessage(),
    getMessages(),
    getPagedMessages(),
    subscribeLastMessage(),
    subscribePagedMessages(),
    subscribeOnMessageExecute(),
    uploadFile(),
    downloadFile()
)

fun cryptonResolvers(): CoroutineContext = scopedResolver()
