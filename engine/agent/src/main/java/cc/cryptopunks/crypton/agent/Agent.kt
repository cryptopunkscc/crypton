package cc.cryptopunks.crypton.agent

import cc.cryptopunks.crypton.ScopeElement
import cc.cryptopunks.crypton.agent.fake.createFakeIdentity
import cc.cryptopunks.crypton.agent.features.broadcastIdentity
import cc.cryptopunks.crypton.agent.features.decodeBytes
import cc.cryptopunks.crypton.agent.features.handleConnections
import cc.cryptopunks.crypton.create.build
import cc.cryptopunks.crypton.create.cryptonContext
import cc.cryptopunks.crypton.create.emission
import cc.cryptopunks.crypton.service.start
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun CoroutineScope.startAgent(
    services: CoroutineContext = EmptyCoroutineContext,
    identity: Identity = createFakeIdentity(services),
) = launch(
    cryptonContext(
//        Dispatchers.IO,
        newSingleThreadContext(identity.id),
        SupervisorJob(),
        ScopeElement(identity.id),
        identity,
        services,
        decodeBytes(),
        broadcastIdentity(),
        handleConnections(),
    ).build()
) {
    emission().start { println(this) }
}
