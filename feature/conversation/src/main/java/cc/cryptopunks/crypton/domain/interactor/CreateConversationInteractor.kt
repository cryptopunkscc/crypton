package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.entity.Conversation
import cc.cryptopunks.crypton.util.Scopes
import kotlinx.coroutines.Job
import javax.inject.Inject

class CreateConversationInteractor @Inject constructor(
    scope: Scopes.UseCase
) : (Conversation) -> Job by {
    scope.launch {

    }
}