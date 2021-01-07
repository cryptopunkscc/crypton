package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.cliv2.Cli
import cc.cryptopunks.crypton.create.cryptonContext
import cc.cryptopunks.crypton.net.clientSocketConnector
import cc.cryptopunks.crypton.service.cryptonFeatures
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking

internal fun main(args: Array<String> = emptyArray()) {
    runBlocking(
        cryptonContext(
            SupervisorJob(),
            Dispatchers.IO,
            CoroutineLog.Label("CliClient"),
        )
    ) {
        cliClient(
            args = args,
            context = Cli.Context(cryptonFeatures().cliCommands())
        ).connect(clientSocketConnector())
    }
}
