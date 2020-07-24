package cc.cryptopunks.crypton.emitter

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.SessionScope
import kotlinx.coroutines.flow.map

internal fun SessionScope.insertInvitationFlow() =
    conferenceInvitationsFlow().map {
        Exec.InsertInvitation(
            address = it.address,
            inviter = it.inviter
        )
    }
