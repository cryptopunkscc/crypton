package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.domain.interactor.SendMessageInteractor
import cc.cryptopunks.crypton.domain.selector.MessagePagedListSelector
import cc.cryptopunks.crypton.module.ViewModelScope
import cc.cryptopunks.crypton.util.Input
import cc.cryptopunks.kache.core.Kache
import cc.cryptopunks.kache.core.KacheManager
import cc.cryptopunks.kache.core.lazy
import javax.inject.Inject

@ViewModelScope
class ChatViewModel @Inject constructor(
    private val sendMessage: SendMessageInteractor,
    private val createMessageViewModel: MessageViewModel.Factory,
    private val messageFlow: MessagePagedListSelector
) : Kache.Provider by KacheManager() {

    val messages get() =  messageFlow(createMessageViewModel)

    val messageInput by lazy<Input>("messageInput")

    fun send() = sendMessage(messageInput.value.text)
}