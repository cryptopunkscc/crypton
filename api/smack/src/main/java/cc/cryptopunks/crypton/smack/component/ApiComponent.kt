package cc.cryptopunks.crypton.smack.component

import cc.cryptopunks.crypton.api.Api
import cc.cryptopunks.crypton.entity.*

internal interface ApiComponent :
    Api,
    Account.Api,
    User.Api,
    Presence.Api,
    Message.Api,
    Chat.Api,
    RosterEvent.Api