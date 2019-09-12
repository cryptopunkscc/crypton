package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.entity.Conversation
import io.reactivex.Completable
import javax.inject.Inject

class CreateConversationInteractor @Inject constructor(
) : (Conversation) -> Completable by {
    Completable.fromAction {

    }
}