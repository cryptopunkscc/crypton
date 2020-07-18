package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.createChat
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach

suspend fun SessionScope.syncConferences(list: Set<Address>) =
    chatRepo.list(listOf(address)).map(Chat::address).let { savedChats ->
        list - savedChats
    }.also {
        log.d("Creating conferences $it")
    }.map { chat ->
        Chat(
            title = chat.local,
            address = chat,
            account = address
        )
    }.asFlow().onEach { chat ->
        createChat(chat)
    }
