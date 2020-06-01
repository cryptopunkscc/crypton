package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow


//internal class ConnectableBinding :
//    Connectable.Binding {
//
//    private val log = typedLog()
//
//    internal val inputChannel = BroadcastChannel<Any>(Channel.BUFFERED)
//
//    internal val outputChannel = BroadcastChannel<Any>(Channel.BUFFERED)
//
//    private val serviceJobs = mutableMapOf<Connectable, Job>()
//
//    override val services get() = serviceJobs.keys
//
//    //    override val output: suspend (Any) -> Unit = outputChannel::send
////
////    override val input: Flow<Any> get() = inputChannel.openSubscription().consumeAsFlow()
////
//
//    override suspend fun cancel() {
//        inputChannel.close()
//        serviceJobs.values.forEach { it.cancel() }
//        serviceJobs.clear()
//    }
//
//    override fun minus(service: Connectable) =
//        serviceJobs.remove(service)?.cancel() != null
//
//    override operator fun plus(service: Connectable?): Boolean =
//        service.ifNotRunning {
//            log.d("Connecting $service")
//            serviceJobs[it] = Connector(
//                input = inputChannel.openSubscription().consumeAsFlow(),
//                output = outputChannel::send
//            ).connect()
//        }
//
//    private fun Connectable?.ifNotRunning(block: Connectable.(Connectable) -> Unit): Boolean = this
//        ?.takeIf {
//            this !in serviceJobs
//        }?.let {
//            block.invoke(it, it)
//        } != null
//}
