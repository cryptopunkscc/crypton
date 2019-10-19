package cc.cryptopunks.crypton.presenter

import androidx.paging.PagedList
import cc.cryptopunks.crypton.actor.Actor
import cc.cryptopunks.crypton.api.Api
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.interactor.SendMessageInteractor
import cc.cryptopunks.crypton.navigation.Navigation
import cc.cryptopunks.crypton.selector.MessagePagedListSelector
import cc.cryptopunks.crypton.util.ExecutorsComponent
import dagger.Provides
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatPresenter @Inject constructor(
    val chat: Chat,
    private val sendMessage: SendMessageInteractor,
    private val createMessagePresenter: MessagePresenter.Factory,
    private val messageFlow: MessagePagedListSelector
) : Presenter<ChatPresenter.View> {

    @dagger.Component(
        dependencies = [
            Address::class,
            Api.Scope::class,
            Message.Api::class,
            Message.Repo::class,
            Chat.Api::class,
            Chat.Repo::class,
            Navigation.Component::class,
            ExecutorsComponent::class
        ],
        modules = [Module::class]
    )
    interface Component : Presenter.Component<ChatPresenter>

    @dagger.Module
    data class Module(@get:Provides val chat: Chat)

    private val send: suspend (String) -> Unit = { sendMessage(it) }

    override suspend fun View.invoke() = coroutineScope {
        launch { sendMessageFlow.collect(send) }
        launch { messageFlow(chat, createMessagePresenter).collect(setMessages) }
    }

    interface View : Actor {
        val sendMessageFlow: Flow<String>
        val setMessages: suspend (PagedList<MessagePresenter>) -> Unit
    }
}