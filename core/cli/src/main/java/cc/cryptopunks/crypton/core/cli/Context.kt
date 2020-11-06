package cc.cryptopunks.crypton.core.cli

import cc.cryptopunks.crypton.cli.CliContext
import cc.cryptopunks.crypton.context.Route

fun context(route: Route<*> = Route.Main) =
    CliContext(
        route = route,
        isRoute = { this is Route<*> },
        empty = { (this as? Route<*>?)?.empty() ?: Unit },
        commands = cryptonCommands
    )
