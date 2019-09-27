package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.domain.selector.LastMessageSelector
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.module.ViewModelScope
import cc.cryptopunks.crypton.util.Navigate
import cc.cryptopunks.crypton.util.letterColors
import javax.inject.Inject

class RosterItemViewModel(
    private val conversation: Chat,
    private val navigate: Navigate,
    private val lastMessage: LastMessageSelector
) {

    val id = conversation.id

    val title get() = conversation.title

    val letter get() = title.first().toLowerCase()

    val avatarColor get() = letterColors.getValue(letter)

    val lastMessageFlow get() = lastMessage(conversation)

    fun onClick() = navigate(TODO())

    @ViewModelScope
    class Factory @Inject constructor(
        private val lastMessage: LastMessageSelector,
        private val navigate: Navigate
    ) : (Chat) -> RosterItemViewModel by { conversation ->
        RosterItemViewModel(
            conversation = conversation,
            lastMessage = lastMessage,
            navigate = navigate
        )
    }
}