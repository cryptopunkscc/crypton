package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.manager.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewAccountConnectedSelector @Inject constructor(
    private val sessionManager: SessionManager
) : () -> Flow<Address> {

    override fun invoke(): Flow<Address> = mutableSetOf<Address>().run {
        sessionManager
            .filter { it.event is Account.Event.Authenticated }
            .filter { add(it.session.address) }
            .map { it.session.address }
    }
}