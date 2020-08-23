package cc.cryptopunks.crypton.internal

import cc.cryptopunks.crypton.Actor
import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.Connector
import cc.cryptopunks.crypton.connector
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job

internal fun Binding.cancel(cause: CancellationException? = null) {
    actorContext.input.cancel(cause)
    serviceContext.input.cancel(cause)
}

internal suspend fun Binding.setActor(connectable: Actor?) = copy(
    actorContext = actorContext.set(connectable)
)

internal suspend fun Binding.setService(connectable: Connectable?) = copy(
    serviceContext = serviceContext.set(connectable)
)

private suspend fun Binding.Context.set(connectable: Connectable?): Binding.Context = apply {
    job?.cancel(CancellationException("Binding change to $connectable"))
}.copy(getConnector().connect(connectable))

private fun Binding.Context.copy(connectorJob: Pair<Connector, Job?>) = copy(
    connector = connectorJob.first,
    job = connectorJob.second
)

private fun Binding.Context.getConnector(): Connector =
    if (job == null) connector else connector(
        input.openSubscription(),
        output
    )

private fun Connector.connect(connectable: Connectable?): Pair<Connector, Job?> =
    this to connectable?.run { connect() }
