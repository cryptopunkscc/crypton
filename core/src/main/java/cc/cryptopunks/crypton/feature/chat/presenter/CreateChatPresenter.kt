package cc.cryptopunks.crypton.feature.chat.presenter

import cc.cryptopunks.crypton.actor.Actor
import cc.cryptopunks.crypton.entity.User
import cc.cryptopunks.crypton.feature.chat.interactor.CreateChatInteractor
import cc.cryptopunks.crypton.navigation.Navigate
import cc.cryptopunks.crypton.navigation.Route
import cc.cryptopunks.crypton.presenter.Presenter
import cc.cryptopunks.crypton.util.cache
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateChatPresenter @Inject constructor(
    private val createChat: CreateChatInteractor,
    private val navigate: Navigate
) : Presenter<CreateChatPresenter.View> {

    private val usersCache = emptyList<User>().cache()

    private val data get() = CreateChatInteractor.Data(
        title = usersCache.value.firstOrNull()?.run { address.id } ?: "test",
        users = usersCache.value
    )

    private val add: suspend (String) -> Unit = { string ->
        usersCache { plus(User(string)) }
    }

    private val remove: suspend (User) -> Unit = { user ->
        usersCache { minus(user) }
    }

    private val create: suspend (Any) -> Unit = {
        createChat(data).runCatching {
            val address = await().address

            navigate(Route.Chat()) {
                chatAddress = address.id
            }
        }
    }

    override suspend fun View.invoke() = coroutineScope {
        launch { addUserClick.onEach(clearInput).collect(add) }
        launch { removeUserClick.collect(remove) }
        launch { usersCache.collect(setUsers) }
        launch { createChatClick.collect(create) }
    }

    interface View : Actor {
        val addUserClick: Flow<String>
        val removeUserClick: Flow<User>
        val createChatClick: Flow<Any>
        val setUsers: suspend (List<User>) -> Unit
        val clearInput: suspend (Any) -> Unit
    }
}