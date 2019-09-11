package cc.cryptopunks.crypton.domain.command

import cc.cryptopunks.crypton.entity.Conversation
import io.reactivex.Completable
import javax.inject.Inject

class CreateConversation @Inject constructor(
) : (Conversation) -> Completable by {
    Completable.fromAction {

    }
}