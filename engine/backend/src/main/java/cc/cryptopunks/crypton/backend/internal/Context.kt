package cc.cryptopunks.crypton.backend.internal

import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Route

internal data class Context(
    val route: Route,
    val binding: Connectable.Binding
)
