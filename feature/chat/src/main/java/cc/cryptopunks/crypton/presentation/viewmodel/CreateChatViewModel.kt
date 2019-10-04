package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.domain.interactor.CreateChat
import cc.cryptopunks.crypton.entity.User
import cc.cryptopunks.crypton.dagger.ViewModelScope
import cc.cryptopunks.crypton.util.Input
import cc.cryptopunks.crypton.util.Navigate
import cc.cryptopunks.crypton.util.error
import cc.cryptopunks.crypton.util.ext.minus
import cc.cryptopunks.crypton.util.ext.plus
import cc.cryptopunks.kache.core.Kache
import cc.cryptopunks.kache.core.KacheManager
import cc.cryptopunks.kache.core.lazy
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@ViewModelScope
class CreateChatViewModel @Inject constructor(
    private val createChat: CreateChat.Interactor,
    private val navigate: Navigate
) : Kache.Provider by KacheManager() {

    val input by lazy<Input>("input")

    val users by lazy<List<User>>("users", initial = emptyList())

    fun addFromInput() = try {
        users + User(input.value.text)
    } catch (throwable: Throwable) {
        input.error = throwable.message ?: throwable.toString()
    }

    fun remove(user: User) = users - user

    fun createChat() = runBlocking {
        createChat(data).invokeOnCompletion { error ->
//            if (error == null) navigate(R.id.navigateChat)
        }

    }

    private val data get() = CreateChat.Data(users = users.value)
}