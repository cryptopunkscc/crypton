package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.core.cli.translateCli
import cc.cryptopunks.crypton.cli.Check
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

class CliClient(
    private val cliInput: Flow<String>
) : Connectable {

    override val coroutineContext = SupervisorJob() + Dispatchers.IO +
        CoroutineLog.Label(javaClass.simpleName)

    override fun Connector.connect(): Job = launch {
        launch {
            cliInput.translateCli().mapNotNull { it.result }.collect { result ->
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
