package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Api
import cc.cryptopunks.crypton.context.Session
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ApiEventSelector @Inject constructor(
    private val session: Session
) : () -> Flow<Api.Event> by { session.netEvents() }
