package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Connector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CliClient : Connectable {

    override val coroutineContext = SupervisorJob() + Dispatchers.IO

    override fun Connector.connect(): Job = launch {
        launch {
            systemInput().translate().collect { result ->
                println(result)
                when (result) {
                    is Throwable -> result.printStackTrace()
                    is Check.Suggest -> println(result)
                    else -> output(result)
                }
            }
        }
        launch {
            input.collect { println(it) }
        }
    }
}
