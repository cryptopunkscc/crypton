package cc.cryptopunks.crypton.feature.chat.viewmodel

import cc.cryptopunks.crypton.entity.Message
import java.util.*
import javax.inject.Inject

data class MessageViewModel(
    private val message: Message
) {
    val text get() = message.text
    val date get() = Date(message.timestamp)

    class Factory @Inject constructor() : (Message) -> MessageViewModel by ::MessageViewModel
}