package cc.cryptopunks.crypton

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

interface Connectable : CoroutineScope {
    fun Connector.connect(): Job
}

fun Connectable.connect(connector: Connector): Job =
    connector.connect()
