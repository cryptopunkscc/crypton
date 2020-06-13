package cc.cryptopunks.crypton.backend.internal

import cc.cryptopunks.crypton.context.AppCore
import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.Route.AccountList
import cc.cryptopunks.crypton.context.Route.AccountManagement
import cc.cryptopunks.crypton.context.Route.Chat
import cc.cryptopunks.crypton.context.Route.CreateChat
import cc.cryptopunks.crypton.context.Route.Dashboard
import cc.cryptopunks.crypton.context.Route.Login
import cc.cryptopunks.crypton.context.Route.Register
import cc.cryptopunks.crypton.context.Route.Roster
import cc.cryptopunks.crypton.context.Route.SetAccount
import cc.cryptopunks.crypton.module.AccountDomainModule
import cc.cryptopunks.crypton.module.ChatServiceModule
import cc.cryptopunks.crypton.module.CommonDomainModule
import cc.cryptopunks.crypton.module.CreateChatServiceModule
import cc.cryptopunks.crypton.module.RosterDomainModule
import kotlinx.coroutines.delay
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
        ).rosterService

        AccountList -> AccountDomainModule(
            appCore = appCore
        ).accountListService

        AccountManagement -> null

        is CreateChat -> CreateChatServiceModule(
            sessionCore = appCore.sessionCore(
                address = CreateChat(route.data).accountAddress
            )
        ).createChatService

        is Chat -> ChatServiceModule(
            chatCore = runBlocking {
                delay(100)
                appCore.sessionCore(
                    address = Chat(route.data).accountAddress
                ).run {
                    chatCore(chatRepo.get(Chat(route.data).address))
                }
            }
        ).chatService

        else -> null
    }
}
