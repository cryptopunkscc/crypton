package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.context.SessionCore
import cc.cryptopunks.crypton.interactor.CreateChatInteractor
import cc.cryptopunks.crypton.service.CreateChatService

class CreateChatServiceModule(
    core: SessionCore
) : SessionCore by core {
    val createChatService by lazy {
        CreateChatService(
            navigate = navigate,
            createChat = CreateChatInteractor(
                repo = chatRepo,
                createChat = session.createChat,
                address = session.address
            )
        )
    }
}
