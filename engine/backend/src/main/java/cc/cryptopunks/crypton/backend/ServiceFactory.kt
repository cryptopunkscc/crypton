package cc.cryptopunks.crypton.backend

import cc.cryptopunks.crypton.context.AppCore
import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.Route.*
import cc.cryptopunks.crypton.module.*
import kotlinx.coroutines.runBlocking

internal class ServiceFactory(
    private val appCore: AppCore
): (Route) -> Connectable? {
    override fun invoke(route: Route): Connectable? = when (route) {

        SetAccount -> CommonDomainModule(
            appCore = appCore
        ).routerService

        Login -> AccountDomainModule(
            appCore = appCore
        ).createAccountService

        Register -> AccountDomainModule(
            appCore = appCore
        ).createAccountService

        Dashboard -> null

        Roster -> RosterDomainModule(
            appCore = appCore
        ).rosterService2

        AccountList -> AccountDomainModule(
            appCore = appCore
        ).accountListService

        AccountManagement -> null

        CreateChat -> CreateChatServiceModule(
            sessionCore = appCore.sessionCore()
        ).createChatService

        is Chat -> ChatServiceModule(
            chatCore = runBlocking {
                appCore.sessionCore().run {
                    chatCore(chatRepo.get(route.address))
                }
            }
        ).chatService

        else -> null
    }
}
