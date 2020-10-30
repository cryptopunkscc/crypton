package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.backend.BackendService
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
import cc.cryptopunks.crypton.core.cliv2.cryptonCommands
import cc.cryptopunks.crypton.net.clientSocketConnector
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun embeddedServer(
    args: Array<out Any> = emptyArray()
): () -> Unit = {
    Cli.Context(
        commands = embeddedCommands,
        defaults = Cli.Config(EmbeddedConfig().default().local())
    )
        .reduce(args.joinArgs())
        .unwrapCliResult().formatCliOutput()?.let(::println)
}

internal class EmbeddedConfig(
    map: Map<String, Any?> = emptyMap()
) : MutableMap<String, Any?> by map.toMutableMap() {
    var home: String by this
    var omemoStore: String by this
    var name: String by this
    var socketAddress: String by this
    var socketPort: Int by this
    var hostAddress: String? by this
    var securityMode: String by this
    var inMemory: String by this
}


internal fun EmbeddedConfig.default() = apply {
    home = "~/.crypton".replaceFirst("~", System.getProperty("user.home"))
    omemoStore = "omemo_store"
    socketPort = 2323
    socketAddress = "127.0.0.1"
    hostAddress = null
    securityMode = "ifPossible"
    inMemory = "false"
}

internal fun EmbeddedConfig.local() = apply {
    hostAddress = "127.0.0.1"
    securityMode = "disabled"
}


private val embeddedCommands: Cli.Commands = commands(
    "-c" to command(
        param(),
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
        runBlocking {
            try {
                val start = cliClient(
                    console = if (interactive.toBoolean())
                        consoleConnector(arrayOf(text)) else
                        flowOf(text).systemFlowConnector(),
                    backend = clientSocketConnector(config),
                    context = Cli.Context(
                        commands = cryptonCommands,
                        defaults = config
                    )
                )
                start()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    },
    "server" to command(
        option("-i").optional().copy(value = false, description = "Run interactive mode"),
        text()
    ) { (interactive, text) ->
        val backend = BackendService(createRootScope(config)).init()
        runBlocking {
            listOf(
                launch {
                    if (interactive.toBoolean()) cliClient(
                        console = consoleConnector(arrayOf(text)),
                        backend = backend.connector(),
                        context = Cli.Context(
                            commands = cryptonCommands,
                            defaults = config
                        )
                    ).invoke()
                },
                launch {
                    initJvmLog()
                    server(
                        config = config,
                        backend = backend
                    ).invoke()
                }
            ).joinAll()
        }
    },
)
