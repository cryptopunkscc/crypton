package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.util.ext.bufferedThrottle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun SessionScope.updateChatNotificationFlow(): Flow<Chat.Service.UpdateNotification> =
    messageRepo.unreadListFlow().bufferedThrottle(500).map {
        Chat.Service.UpdateNotification(address, it.flatten())
    }
