package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal fun RootScope.reconnectSessions(): List<Job> =
    sessions.get().values.map { session ->
        launch { session.reconnectIfNeeded() }
    }

internal suspend fun SessionScope.reconnectIfNeeded(): Unit = try {
    log.d { "reconnecting: $address" }
    if (!isConnected()) {
        connect()
        if (!isAuthenticated()) login()
        initOmemo()
    }
    Unit
} catch (e: Throwable) {
    log.d { "reconnection failed: $e" }
    interrupt()
}

internal tailrec suspend fun SessionScope.reconnectIfNeeded(
    retryCount: Int = -1,
    delayInMillis: Long = 3000
): Throwable? {
    val result = try {
        log.d { "reconnecting: $address" }
        if (!isConnected()) {
            connect()
            if (!isAuthenticated()) login()
            initOmemo()
        }
        null
    } catch (e: Throwable) {
        log.d { "reconnection failed: $e" }
        e
    }
    return if (result == null || retryCount == 0) result else {
        delay(delayInMillis)
        reconnectIfNeeded(retryCount - 1, delayInMillis)
    }
}
