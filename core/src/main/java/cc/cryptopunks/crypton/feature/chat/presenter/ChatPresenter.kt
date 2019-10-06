package cc.cryptopunks.crypton.feature.chat.presenter

import androidx.paging.PagedList
import cc.cryptopunks.crypton.feature.chat.interactor.SendMessageInteractor
import cc.cryptopunks.crypton.feature.chat.selector.MessagePagedListSelector
import cc.cryptopunks.crypton.util.Presenter
import cc.cryptopunks.kache.core.Kache
import cc.cryptopunks.kache.core.KacheManager
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatPresenter @Inject constructor(
    private val sendMessage: SendMessageInteractor,
    private val createMessagePresenter: MessagePresenter.Factory,
    private val messageFlow: MessagePagedListSelector
) : Kache.Provider by KacheManager(),
    Presenter<ChatPresenter.View> {

    private val send: suspend (String) -> Unit = { sendMessage(it) }

    override suspend fun View.invoke() = coroutineScope {
        launch { sendMessageFlow.collect(send) }
        launch { messageFlow(createMessagePresenter).collect(setMessages) }
    }

    interface View {
        val sendMessageFlow: Flow<String>
        val setMessages: suspend (PagedList<MessagePresenter>) -> Unit
    }
}