package cc.cryptopunks.crypton.smack.component

import cc.cryptopunks.crypton.net.Net
import cc.cryptopunks.crypton.entity.*

internal interface NetComponent :
    Net,
    Account.Net,
    User.Net,
    Presence.Net,
    Message.Net,
    Chat.Net,
    RosterEvent.Net