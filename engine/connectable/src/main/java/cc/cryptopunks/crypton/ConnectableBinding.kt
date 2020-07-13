package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.internal.Binding
import cc.cryptopunks.crypton.internal.cancel
import cc.cryptopunks.crypton.internal.createBinding
import cc.cryptopunks.crypton.internal.setActor
import cc.cryptopunks.crypton.internal.setService
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
