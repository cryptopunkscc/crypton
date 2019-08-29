package cc.cryptopunks.crypton.conversation.presentation.viewmodel

import cc.cryptopunks.crypton.conversation.domain.query.LastMessage
import cc.cryptopunks.crypton.core.entity.Conversation
import cc.cryptopunks.crypton.core.module.ViewModelScope
import cc.cryptopunks.crypton.core.util.Navigate
import cc.cryptopunks.crypton.core.util.letterColors
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