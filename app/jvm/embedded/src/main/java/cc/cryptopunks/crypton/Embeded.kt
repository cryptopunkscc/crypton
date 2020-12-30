package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.cliv2.Cli
import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.commands
import cc.cryptopunks.crypton.cliv2.joinArgs
import cc.cryptopunks.crypton.cliv2.option
import cc.cryptopunks.crypton.cliv2.optional
import cc.cryptopunks.crypton.cliv2.param
import cc.cryptopunks.crypton.cliv2.raw
import cc.cryptopunks.crypton.cliv2.reduce
import cc.cryptopunks.crypton.cliv2.text
import cc.cryptopunks.crypton.cliv2.unwrapCliResult
import cc.cryptopunks.crypton.context.Subscribe
import cc.cryptopunks.crypton.net.clientSocketConnector
import cc.cryptopunks.crypton.service.cryptonFeatures
import cc.cryptopunks.crypton.service.start
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun embeddedServer(
    args: Array<out Any> = emptyArray(),
): () -> Unit = {
    Cli.Context(
        commands = embeddedCommands,
        defaults = Cli.Config(
            EmbeddedConfig()
                .default()
//                .local()
        )
    )
        .reduce(args.joinArgs())
        .unwrapCliResult()
        .formatCliOutput()
        .let(::println)
}


private val embeddedCommands: Cli.Commands = commands(
    "-c" to command(
        param(),
        name = "-c",
        description = "Specify configuration file path."
    ).raw {
        copy()
    },
    "cli" to command(
        option("-i").optional().copy(value = false, description = "Run interactive mode"),
        text(),
        description = "Run cli client."
    ) { (interactive, text) ->
        println(text)
        runBlocking(
            Dispatchers.IO + CoroutineLog.Label("CliClient")
        ) {
            cliClient(
                args = arrayOf(text),
                interactive = interactive.toBoolean(),
                context = Cli.Context(
                    commands = cryptonFeatures().cliCommands(),
                    defaults = config
                )
            ).connect(clientSocketConnector(config))
        }
    },
    "server" to command(
        option("-i").optional().copy(value = false, description = "Run interactive mode"),
        text()
    ) { (interactive, text) ->
        runBlocking {
            initJvmLog()
//            val backend = BackendService(createServerScope(config))

            createServerScope(config).run {
                listOf(
                    launch { Subscribe.AppService.start { println(this) } },
                    launch { server(config) },
                    launch {
                        cliClient(
                            args = arrayOf(text),
                            interactive = interactive.toBoolean(),
                            context = Cli.Context(
                                commands = cryptonFeatures().cliCommands(),
                                defaults = config
                            )
                        ).start()
                    },
                )
            }.joinAll()
//                launch {
//                    if (interactive.toBoolean()) cliClient(
//                        console = consoleConnector(arrayOf(text)),
//                        backend = backend.connector(),
//                        context = Cli.Context(
//                            commands = backend.scope.coroutineContext.cliCommands(),
//                            defaults = config
//                        )
//                    ).invoke()
//                }
        }
    },
)
