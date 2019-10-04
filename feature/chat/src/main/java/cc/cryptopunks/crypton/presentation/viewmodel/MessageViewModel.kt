package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.dagger.ViewModelScope
import java.util.*
import javax.inject.Inject

data class MessageViewModel(
    private val message: Message
) {
    val text get() = message.text
    val date get() = Date(message.timestamp)

    @ViewModelScope
    class Factory @Inject constructor() : (Message) -> MessageViewModel by ::MessageViewModel
}