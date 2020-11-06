package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter

fun Flow<Message>.filterByStatus(vararg required: Message.Status) =
    filter { it.status in required }
