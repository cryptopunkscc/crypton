package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Network

internal class SessionInteractor(
    private val reconnect: ReconnectSessionsInteractor,
    private val interrupt: InterruptSessionsInteractor
) {
    suspend operator fun invoke(status: Network.Status) {
        when (status) {
            is Network.Status.Available,
            is Network.Status.Changed -> reconnect()
            is Network.Status.Unavailable -> interrupt()
        }
    }
}
