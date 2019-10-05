package cc.cryptopunks.crypton.feature.chat.viewmodel

import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.feature.chat.selector.LastMessageSelector
import cc.cryptopunks.crypton.util.Navigate
import javax.inject.Inject

class RosterItemViewModel(
    private val conversation: Chat,
    private val navigate: Navigate,
    private val lastMessage: LastMessageSelector
) {

    val id = conversation.id

    val title get() = conversation.title

    val letter get() = title.first().toLowerCase()

    val lastMessageFlow get() = lastMessage(conversation)

    fun onClick() = navigate(TODO())

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