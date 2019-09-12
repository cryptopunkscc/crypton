package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.domain.selector.LastMessage
import cc.cryptopunks.crypton.entity.Conversation
import cc.cryptopunks.crypton.module.ViewModelScope
import cc.cryptopunks.crypton.util.Navigate
import cc.cryptopunks.crypton.util.letterColors
import javax.inject.Inject

class ConversationItemViewModel(
    private val conversation: Conversation,
    private val navigate: Navigate,
    private val lastMessage: LastMessage
) {

    val id = conversation.id

    val title get() = conversation.title

    val letter get() = title.first().toLowerCase()

    val avatarColor get() = letterColors.getValue(letter)

    val lastMessageObservable get() = lastMessage(conversation)

    fun onClick() = navigate(TODO())

    @ViewModelScope
    class Factory @Inject constructor(
        private val lastMessage: LastMessage,
        private val navigate: Navigate
    ) : (Conversation) -> ConversationItemViewModel by { conversation ->
        ConversationItemViewModel(
            conversation = conversation,
            lastMessage = lastMessage,
            navigate = navigate
        )
    }
}