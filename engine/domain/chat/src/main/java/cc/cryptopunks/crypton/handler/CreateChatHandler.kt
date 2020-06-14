package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.context.User
import cc.cryptopunks.crypton.context.handle
import cc.cryptopunks.crypton.interactor.CreateChatInteractor
import kotlinx.coroutines.launch

internal fun Session.handleCreateChat(
    createChat: CreateChatInteractor,
    navigate: Route.Navigate
) = handle<Chat.Service.CreateChat> { output ->
    scope.launch {
        try {
            val data = CreateChatInteractor.Data(
                title = address.id,
                users = listOf(User(address))
            )
            createChat(data).run {
                Route.Chat().also {
                    it.chatAddress = address.id
                }.let(navigate)
            }
        } catch (throwable: Throwable) {
            output(throwable)
        }
    }
}
