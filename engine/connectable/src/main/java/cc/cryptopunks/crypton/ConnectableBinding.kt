package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.internal.Binding
import cc.cryptopunks.crypton.internal.cancel
import cc.cryptopunks.crypton.internal.createBinding
import cc.cryptopunks.crypton.internal.setActor
import cc.cryptopunks.crypton.internal.setService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking

class ConnectableBinding(
    actor: BroadcastChannel<Any> = BroadcastChannel(Channel.BUFFERED),
    service: BroadcastChannel<Any> = BroadcastChannel(Channel.BUFFERED)
) :
    Connectable.Binding {

    private var binding: Binding = createBinding(
        actorChannel = actor,
        serviceChannel = service
    )

    val sendToService: suspend (Any) -> Unit = service::send

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

    override fun minus(service: Connectable?): Boolean =
        if (service == null) false else runBlocking {
            services -= service
            binding = binding.run {
                if (service is Actor)
                    setActor(null) else
                    setService(null)
            }
            true
        }

    override suspend fun cancel(cause: CancellationException?) {
        services.clear()
        binding.cancel(cause)
        binding = createBinding()
    }
}
