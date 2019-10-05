package cc.cryptopunks.crypton.feature.chat.viewmodel

import cc.cryptopunks.crypton.feature.chat.interactor.SendMessageInteractor
import cc.cryptopunks.crypton.feature.chat.selector.MessagePagedListSelector
import cc.cryptopunks.crypton.util.Input
import cc.cryptopunks.kache.core.Kache
import cc.cryptopunks.kache.core.KacheManager
import cc.cryptopunks.kache.core.lazy
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val sendMessage: SendMessageInteractor,
    private val createMessageViewModel: MessageViewModel.Factory,
    private val messageFlow: MessagePagedListSelector
) : Kache.Provider by KacheManager() {

    val messages get() =  messageFlow(createMessageViewModel)

    val messageInput by lazy<Input>("messageInput")

    fun send() = sendMessage(messageInput.value.text)
}