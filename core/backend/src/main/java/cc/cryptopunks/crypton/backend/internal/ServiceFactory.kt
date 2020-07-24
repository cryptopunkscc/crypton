package cc.cryptopunks.crypton.backend.internal

import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.connectable
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.Route.Chat
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

internal class ServiceFactory(
    private val appScope: AppScope
) : (Route<*>) -> Connectable? {
    override fun invoke(route: Route<*>): Connectable? = when (route) {

        Route.Main -> appScope

        is Chat -> runBlocking {
            delay(100)
            appScope.sessionScope(route.account).chatScope(route.address)
        }

        else -> null
    }?.connectable()
}


