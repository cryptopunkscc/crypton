package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.util.ext.bufferedThrottle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun SessionScope.updateChatNotificationFlow(): Flow<Exec.UpdateNotification> =
    messageRepo.unreadListFlow().bufferedThrottle(500).map {
        Exec.UpdateNotification(it.flatten())
    }
