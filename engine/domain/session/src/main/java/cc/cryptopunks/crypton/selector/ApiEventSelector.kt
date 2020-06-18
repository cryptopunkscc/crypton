package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Api
import cc.cryptopunks.crypton.context.SessionScope
import kotlinx.coroutines.flow.Flow

internal class ApiEventSelector(
    private val session: SessionScope
) : () -> Flow<Api.Event> by { session.netEvents() }
