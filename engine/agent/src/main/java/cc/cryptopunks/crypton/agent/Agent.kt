package cc.cryptopunks.crypton.agent

import cc.cryptopunks.crypton.ScopeElement
import cc.cryptopunks.crypton.agent.fake.fakeBroadcast
import cc.cryptopunks.crypton.agent.fake.fakeSocketConnections
import cc.cryptopunks.crypton.agent.fake.openFakeConnection
import cc.cryptopunks.crypton.agent.features.broadcastIdentity
import cc.cryptopunks.crypton.agent.features.decodeBytes
import cc.cryptopunks.crypton.agent.features.handleConnections
import cc.cryptopunks.crypton.create.build
import cc.cryptopunks.crypton.create.cryptonContext
import cc.cryptopunks.crypton.create.dep
import cc.cryptopunks.crypton.create.emission
import cc.cryptopunks.crypton.service.start
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun CoroutineScope.startAgent(
    services: CoroutineContext = EmptyCoroutineContext,
    identity: Identity = createFakeIdentity(services),
) = launch(
    cryptonContext(
        Dispatchers.IO,
        SupervisorJob(),
        ScopeElement(identity.id),
        identity,
        services,
        decodeBytes(),
        broadcastIdentity(),
        handleConnections(),
        fakeBroadcast().dep(BROADCAST),
        fakeSocketConnections.dep(SOCKET_CONNECTIONS),
        openFakeConnection.dep(OPEN_CONNECTION)
    ).build()
) {
    emission().start { println(this) }
}
