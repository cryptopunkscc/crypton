package cc.cryptopunks.crypton

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CliClient(
    private val cli: Flow<String>
) : Connectable {

    override val coroutineContext = SupervisorJob() + Dispatchers.IO

    override fun Connector.connect(): Job = launch {
        launch {
            cli.translateCli().collect { result ->
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
