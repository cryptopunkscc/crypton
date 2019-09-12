package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.domain.interactor.CreateConversationInteractor
import cc.cryptopunks.crypton.entity.User
import cc.cryptopunks.crypton.module.ViewModelScope
import cc.cryptopunks.crypton.util.AsyncExecutor
import cc.cryptopunks.crypton.util.Input
import cc.cryptopunks.crypton.util.ext.minus
import cc.cryptopunks.crypton.util.ext.plus
import cc.cryptopunks.kache.core.Kache
import cc.cryptopunks.kache.core.KacheManager
import cc.cryptopunks.kache.core.lazy
import javax.inject.Inject

@ViewModelScope
class CreateConversationViewModel @Inject constructor(
    private val async: AsyncExecutor,
    private val createConversation: CreateConversationInteractor
) : Kache.Provider by KacheManager() {

    val users by lazy<List<User>>("users", initial = emptyList())

    val userInput by lazy<Input>("userInput")

    fun addFromInput() {
        users + User.from(userInput.value.text)
    }

    fun remove(user: User) {
        users - user
    }

    fun createConversation() {
        async(task = createConversation)
    }
}