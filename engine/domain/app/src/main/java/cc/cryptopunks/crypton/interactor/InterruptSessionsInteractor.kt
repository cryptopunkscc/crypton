package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Session

internal class InterruptSessionsInteractor(
    private val sessionStore: Session.Store
) {
    operator fun invoke() {
        sessionStore.get().values.forEach { session ->
            session.interrupt()
        }
    }
}
