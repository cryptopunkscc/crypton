package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.emitter
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.interactor.insertChat
import kotlinx.coroutines.flow.map

internal fun insertInvitation() = feature(

    emitter = emitter<SessionScope> {
        conferenceInvitationsFlow().map {
            Exec.InsertInvitation(
                address = it.address,
                inviter = it.inviter
            )
        }
    },

    handler = { _, arg: Exec.InsertInvitation ->
        if (!chatRepo.contains(arg.address)) {
            insertChat(
                Chat(
                    address = arg.address,
                    account = account.address
                )
            )
        }
    }
)
