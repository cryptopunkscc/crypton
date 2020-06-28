package cc.cryptopunks.crypton.backend.internal

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.Route.Chat
import cc.cryptopunks.crypton.context.createHandlers
import cc.cryptopunks.crypton.service.accountHandlers
import cc.cryptopunks.crypton.service.chatService
import cc.cryptopunks.crypton.service.createChatHandlers
import cc.cryptopunks.crypton.service.rosterHandlers
import cc.cryptopunks.crypton.util.service
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

internal class ServiceFactory(
    private val appScope: AppScope
) : (Route) -> Connectable? {
    override fun invoke(route: Route): Connectable? = when (route) {

        Route.Main -> mainService(appScope)

        is Chat -> chatService(
            scope = runBlocking {
                delay(100)
                appScope.sessionScope(
                    address = route.account
                ).run {
                    chatScope(chatRepo.get(route.address))
                }
            }
        )

        else -> null
    }
}


private fun mainService(scope: AppScope) = service(scope) {
    createHandlers {
        +accountHandlers()
        +rosterHandlers()
        +createChatHandlers()
    }
}
