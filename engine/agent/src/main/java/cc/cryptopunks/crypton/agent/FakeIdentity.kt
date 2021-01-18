package cc.cryptopunks.crypton.agent

import cc.cryptopunks.crypton.Handler
import cc.cryptopunks.crypton.agent.fake.fakeEndpoint
import cc.cryptopunks.crypton.util.toHex
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.random.Random

fun createFakeIdentity(context: CoroutineContext = EmptyCoroutineContext) = Identity(randomId()).run {
    copy(
        endpoints = setOf(fakeEndpoint(id)),
        services = context.serviceTypes()
    )
}

private fun randomId(): IdentityId = Random.nextBytes(4).toHex()

private fun CoroutineContext.serviceTypes() = fold(emptySet<String>()) { set, element ->
    if (element !is Handler<*>) set
    else set + (element.key.type as Class<*>).name
}
