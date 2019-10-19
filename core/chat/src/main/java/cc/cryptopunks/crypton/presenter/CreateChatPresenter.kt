package cc.cryptopunks.crypton.presenter

import cc.cryptopunks.crypton.actor.Actor
import cc.cryptopunks.crypton.api.Api
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.User
import cc.cryptopunks.crypton.interactor.CreateChatInteractor
import cc.cryptopunks.crypton.navigation.Navigate
import cc.cryptopunks.crypton.navigation.Navigation
import cc.cryptopunks.crypton.navigation.Route
import cc.cryptopunks.crypton.util.cache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateChatPresenter @Inject constructor(
    private val createChat: CreateChatInteractor,
    private val navigate: Navigate
) : Presenter<CreateChatPresenter.View> {

    @Singleton
    @dagger.Component(dependencies = [
        Address::class,
        Api.Scope::class,
        Chat.Api::class,
        Chat.Repo::class,
        Navigation.Component::class
    ])
    interface Component : Presenter.Component<CreateChatPresenter>

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

                navigate(Route.Chat()) {
                    chatAddress = address.id
                }
            }
        }.exceptionOrNull()
    }

    override suspend fun View.invoke() = supervisorScope {
        launch { init() }
        launch { addUserClick.onEach(clearInput).collect(add) }
        launch { removeUserClick.collect(remove) }
        launch { usersCache.collect(setUsers) }
        launch { createChatClick.map(create).collect(setError) }
    }

    interface View : Actor {
        suspend fun init()
        val addUserClick: Flow<User>
        val removeUserClick: Flow<User>
        val createChatClick: Flow<Any>
        val setUsers: suspend (List<User>) -> Unit
        val clearInput: suspend (Any) -> Unit
        val setError: suspend (Throwable?) -> Unit
    }
}