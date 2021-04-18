package cc.cryptopunks.crypton.agent.fake

import cc.cryptopunks.crypton.Handler
import cc.cryptopunks.crypton.agent.Identity
import cc.cryptopunks.crypton.agent.IdentityId
import cc.cryptopunks.crypton.util.toHex
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.random.Random

fun createFakeIdentity(context: CoroutineContext = EmptyCoroutineContext) = Identity(randomId()).run {
    copy(
        endpoints = setOf(fakeEndpoint(id)),
        services = context.getServiceTypes()
    )
}

private fun randomId(): IdentityId = Random.nextBytes(4).toHex()

private fun CoroutineContext.getServiceTypes() = fold(emptySet<String>()) { set, element ->
    if (element !is Handler<*>) set
    else set + (element.key.type as Class<*>).name
}
