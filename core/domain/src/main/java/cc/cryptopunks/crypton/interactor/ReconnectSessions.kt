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

internal suspend fun SessionScope.reconnectIfNeeded(): Unit =
    net.run {
        try {
            log.d { "reconnecting: $account" }
            if (!isConnected()) {
                connect()
                accountNet.run {
                    if (!isAuthenticated()) login()
                }
                initOmemo()
            }
        } catch (e: Throwable) {
            log.d { "reconnection failed: $e" }
            interrupt()
        }
    }

internal tailrec suspend fun SessionScope.reconnectIfNeeded(
    retryCount: Int = -1,
    delayInMillis: Long = 3000,
): Throwable? {
    val result = try {
        if (!networkSys.status.isConnected) {
            log.d { "skip reconnecting: $account, network status: ${networkSys.status}." }
        } else net.run {
            if (!isConnected()) {
                log.d { "reconnecting: $account" }
                connect()
                accountNet.run {
                    if (!isAuthenticated()) login()
                }
                initOmemo()
            }
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
