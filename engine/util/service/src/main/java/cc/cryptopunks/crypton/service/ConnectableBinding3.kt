package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Actor
import cc.cryptopunks.crypton.context.Connectable
import kotlinx.coroutines.runBlocking

class ConnectableBinding3(
    channels: Channels = Channels()
) :
    Connectable.Binding {

    private var actorServiceBinding: ActorServiceBinding = actorServiceBinding(
        actorChannel = channels.actor,
        serviceChannel = channels.service
    )

    val sendToService: suspend (Any) -> Unit = channels.service::send

    override val services: MutableSet<Connectable> = mutableSetOf()

    override fun send(any: Any) = runBlocking { sendToService(any) }

    override fun plus(service: Connectable?): Boolean = runBlocking {
        if (service != null) services += service
        actorServiceBinding = actorServiceBinding.run {
            if (service is Actor)
                setActor(service) else
                setService(service)
        }
        service != null
    }

    override fun minus(service: Connectable): Boolean = runBlocking {
        services -= service
        actorServiceBinding = actorServiceBinding.run {
            if (service is Actor)
                setActor(null) else
                setService(null)
        }
        true
    }

    override suspend fun cancel() {
        actorServiceBinding.cancel()
        actorServiceBinding = actorServiceBinding()
    }

}
