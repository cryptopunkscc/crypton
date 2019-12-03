package cc.cryptopunks.crypton.presenter

import cc.cryptopunks.crypton.context.Presenter
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.User
import cc.cryptopunks.crypton.interactor.CreateChatInteractor
import cc.cryptopunks.crypton.util.cache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

class CreateChatPresenter @Inject constructor(
    private val createChat: CreateChatInteractor,
    private val navigate: Route.Api.Navigate
) : Presenter<CreateChatPresenter.Actor> {

    private val usersCache = emptyList<User>().cache()

    private val data
        get() = CreateChatInteractor.Data(
            title = usersCache.value.firstOrNull()?.run { address.id } ?: "test",
            users = usersCache.value
        )

    private val add: suspend (User) -> Unit = { user ->
        user.address.validate()
        usersCache { plus(user) }
    }

    private val remove: suspend (User) -> Unit = { user ->
        usersCache { minus(user) }
    }

    private val create: suspend (Any) -> Throwable? = { arg ->
        runCatching {
            if (arg is User) add(arg)

            createChat(data).run {
                val address = await().address
                Route.Chat()
                    .apply { chatAddress = address.id }
                    .let(navigate)
            }
        }.exceptionOrNull()
    }

    override suspend fun Actor.invoke() = supervisorScope {
        launch { init() }
        launch { addUserClick.onEach(clearInput).collect(add) }
        launch { removeUserClick.collect(remove) }
        launch { usersCache.collect(setUsers) }
        launch { createChatClick.map(create).collect(setError) }
    }

    interface Actor {
        suspend fun init()
        val addUserClick: Flow<User>
        val removeUserClick: Flow<User>
        val createChatClick: Flow<Any>
        val setUsers: suspend (List<User>) -> Unit
        val clearInput: suspend (Any) -> Unit
        val setError: suspend (Throwable?) -> Unit
    }

    interface Core {
        val createChatPresenter: CreateChatPresenter
    }
}