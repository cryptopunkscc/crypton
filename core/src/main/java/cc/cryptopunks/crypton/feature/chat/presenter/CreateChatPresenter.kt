package cc.cryptopunks.crypton.feature.chat.presenter

import cc.cryptopunks.crypton.entity.User
import cc.cryptopunks.crypton.feature.Route
import cc.cryptopunks.crypton.feature.chat.interactor.CreateChat
import cc.cryptopunks.crypton.util.Navigate
import cc.cryptopunks.crypton.util.Presenter
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateChatPresenter @Inject constructor(
    private val createChat: CreateChat.Interactor,
    private val navigate: Navigate
) : Presenter<CreateChatPresenter.View> {

    private val usersBroadcast = BroadcastChannel<List<User>>(Channel.CONFLATED)
    private var users = emptyList<User>()
    private val data get() = CreateChat.Data(users = users)


    private val add: suspend (String) -> Unit = { string ->
        users = users + User(string)
        usersBroadcast.send(users)
    }

    private val remove: suspend (User) -> Unit = { user ->
        users = users + user
        usersBroadcast.send(users)
    }

    private val create: suspend (Any) -> Unit = {
        createChat(data).invokeOnCompletion { error ->
            if (error == null) navigate(Route.Chat())
        }
    }

    override suspend fun View.invoke() = coroutineScope {
        launch { addUserClick.onEach(clearInput).collect(add) }
        launch { removeUserClick.collect(remove) }
        launch { usersBroadcast.asFlow().collect(setUsers) }
        launch { createChatClick.collect(create) }
    }

    interface View {
        val addUserClick: Flow<String>
        val removeUserClick: Flow<User>
        val createChatClick: Flow<Any>
        val setUsers: suspend (List<User>) -> Unit
        val clearInput: suspend (Any) -> Unit
    }
}

