package cc.cryptopunks.crypton.cli

import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.translator.Context

fun context(route: Route<*> = Route.Main) =
    Context(
        route = route,
        isRoute = { this is Route<*> },
        empty = { (this as? Route<*>?)?.empty() ?: Unit },
        commands = cryptonCommands
    )
