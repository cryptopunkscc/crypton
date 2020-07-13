package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Actor
import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.service.internal.Binding
import cc.cryptopunks.crypton.service.internal.cancel
import cc.cryptopunks.crypton.service.internal.createBinding
import cc.cryptopunks.crypton.service.internal.setActor
import cc.cryptopunks.crypton.service.internal.setService
import kotlinx.coroutines.runBlocking

class ConnectableBinding(
    channels: Channels = Channels()
) :
    Connectable.Binding {

    private var binding: Binding = createBinding(
        actorChannel = channels.actor,
        serviceChannel = channels.service
    )

    val sendToService: suspend (Any) -> Unit = channels.service::send

    override val services: MutableSet<Connectable> = mutableSetOf()

    override fun send(any: Any) = runBlocking { sendToService(any) }

    override fun plus(service: Connectable?): Boolean = runBlocking {
        if (service != null) services += service
        binding = binding.run {
            if (service is Actor)
                setActor(service) else
                setService(service)
        }
        service != null
    }

    override fun minus(service: Connectable): Boolean = runBlocking {
        services -= service
        binding = binding.run {
            if (service is Actor)
                setActor(null) else
                setService(null)
        }
        true
    }

    override suspend fun cancel() {
        binding.cancel()
        binding = createBinding()
    }
}
