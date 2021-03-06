package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.cliv2.Cli
import cc.cryptopunks.crypton.core.cliv2.cryptonCommands
import cc.cryptopunks.crypton.net.clientSocketConnector
import kotlinx.coroutines.runBlocking

internal fun main(args: Array<String> = emptyArray()) {
    runBlocking {
//        initJvmLog()
        cliClient(
            console = consoleConnector(args),
            backend = clientSocketConnector(),
            context = Cli.Context(cryptonCommands)
        ).invoke()
    }
}
