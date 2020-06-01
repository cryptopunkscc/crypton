package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Actor
import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Connector
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.consumeAsFlow

fun actorServiceBinding(
    actorChannel: BroadcastChannel<Any> = BroadcastChannel(Channel.BUFFERED),
    serviceChannel: BroadcastChannel<Any> = BroadcastChannel(Channel.BUFFERED)
) = ActorServiceBinding(
    actorContext = connectableHolder(
        input = actorChannel,
        output = serviceChannel
    ),
    serviceContext = connectableHolder(
        input = serviceChannel,
        output = actorChannel
    )
)

suspend fun ActorServiceBinding.setActor(connectable: Actor?) = copy(
    actorContext = actorContext.set(connectable)
)

suspend fun ActorServiceBinding.setService(connectable: Connectable?) = copy(
    serviceContext = serviceContext.set(connectable)
)

fun ActorServiceBinding.cancel() {
    actorContext.input.cancel()
    serviceContext.input.cancel()
}

data class ActorServiceBinding(
    val actorContext: ConnectableContext,
    val serviceContext: ConnectableContext
)

data class ConnectableContext(
    val input: BroadcastChannel<Any>,
    val output: BroadcastChannel<Any>,
    val connector: Connector,
    val job: Job? = null
)

private fun connectableHolder(
    input: BroadcastChannel<Any>,
    output: BroadcastChannel<Any>
) = ConnectableContext(
    input = input,
    output = output,
    connector = connector(
        inputChannel = input.openSubscription(),
        outputChannel = output
    )
)

private suspend fun ConnectableContext.set(connectable: Connectable?): ConnectableContext = this
    .apply { job?.cancelAndJoin() }
    .copy(getConnector().connect(connectable))

fun ConnectableContext.copy(connectorJob: Pair<Connector, Job?>) = copy(
    connector = connectorJob.first,
    job = connectorJob.second
)

private fun ConnectableContext.getConnector() =
    if (job == null)
        connector else
        connector(input.openSubscription(), output)

fun connector(
    inputChannel: ReceiveChannel<Any>,
    outputChannel: SendChannel<Any>
) = Connector(
    input = inputChannel.consumeAsFlow(),
    output = { outputChannel.send(it) }
)


private fun Connector.connect(connectable: Connectable?): Pair<Connector, Job?> =
    this to connectable?.run { connect() }
