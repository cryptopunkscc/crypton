package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.context.User

internal suspend fun Session.createChat(data: Chat.Service.CreateChatData) =
    data.run {
        log.d("Chat $users creating")
        validate()
        Chat(
            title = title,
            users = users.map(::User) + User(address),
            account = address
        )
    }.run {
        if (!isDirect)
            createChat(this) else
            copy(address = users.first().address)
    }.also { chat ->
        chatRepo.insertIfNeeded(chat)
        log.d("Chat ${chat.address} with users ${chat.users} created")
    }
