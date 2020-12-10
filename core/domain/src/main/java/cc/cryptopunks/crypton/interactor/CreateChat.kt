package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.util.logger.log

suspend fun SessionScope.createChat(chat: Chat) {
    log.d { "Creating $chat" }
    chatNet.run {
        if (chat.isConference && chat.address !in listJoinedRooms())
            createOrJoinConference(chat)
    }
    log.d { "Chat ${chat.address} with users ${chat.users} created" }
    insertChat(chat)
}

suspend fun SessionScope.insertChat(chat: Chat) {
    log.d { "Inserting $chat" }
    chatRepo.insertIfNeeded(chat)
    log.d { "Chat ${chat.address} with users ${chat.users} Inserted" }
}
