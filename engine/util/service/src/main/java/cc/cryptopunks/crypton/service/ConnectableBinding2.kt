package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Actor
import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Connector
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking


//internal class ConnectableBinding2(
//    private val channels: Channels = Channels()
//) : Connectable.Binding {
//    private val serviceJobs = mutableMapOf<Connectable, Job>()
//
//    val sendToService: suspend (Any) -> Unit = channels.service::send
//    val sendToActor: suspend (Any) -> Unit = channels.actor::send
//
//    private val serviceReservations = Channel<Connector>(Channel.BUFFERED)
//    private val actorReservations = Channel<Connector>(Channel.BUFFERED)
//
//    fun reserve(services: Int = 1, actors: Int = 1) {
//        repeat(services) { serviceReservations.offer(channels.serviceConnector()) }
//        repeat(actors) { actorReservations.offer(channels.actorConnector()) }
//    }
//
//    override val services: Set<Connectable> = serviceJobs.keys
//
//    override suspend fun cancel() {
//        channels.cancel()
//        serviceJobs.values.forEach { it.cancel() }
//        serviceJobs.clear()
//    }
//
//    override fun minus(service: Connectable): Boolean = runBlocking {
//        serviceJobs.remove(service)?.cancelAndJoin() != null
//    }
//
//    override fun plus(service: Connectable?): Boolean =
//        service?.append() != null
//
//    private fun Connectable.append(): Job =
//        serviceJobs.getOrPut(this) {
//            channels.run {
//                if (this@append is Actor)
//                    actorConnector() else
//                    serviceConnector()
//            }.connect()
//        }
//}


data class Channels(
    val actor: BroadcastChannel<Any> = BroadcastChannel(Channel.BUFFERED),
    val service: BroadcastChannel<Any> = BroadcastChannel(Channel.BUFFERED)
) {
    fun actorConnector() = connector(actor.openSubscription(), service)
    fun serviceConnector() = connector(service.openSubscription(), actor)
    fun cancel() {
        actor.cancel()
        service.cancel()
    }
}
