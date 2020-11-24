package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.NonHandle
import cc.cryptopunks.crypton.cli.configure
import cc.cryptopunks.crypton.cliv2.Cli
import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.named
import cc.cryptopunks.crypton.cliv2.param
import cc.cryptopunks.crypton.cliv2.raw
import cc.cryptopunks.crypton.feature

internal fun cliConfigure() = feature(

    command = command(
        named("account").copy(optional = true),
        named("chat").copy(optional = true),
        name = "configure cli"
    ).raw { args ->
        if (args.filterNotNull().isEmpty()) copy(
            result = Cli.Result.Return(config.toMap())
        ) else configure {
            args.getOrNull(0)?.run { account = toString() }
            args.getOrNull(1)?.run { chat = toString() }
        }
    },

    handler = NonHandle
)

internal fun cliSetAccount() = feature(

    command = command(
        param().copy(name = "account@domain.com", description = "Account address"),
        name = "a",
        description = "Set current account to config."
    ).raw { (a) ->
        configure { account = a?.toString()?.takeIf { it.isNotBlank() } }
    },

    handler = NonHandle
)

internal fun cliSetChat() = feature(

    command = command(
        param().copy(name = "chat@domain.com", description = "Chat address"),
        name = "c",
        description = "Set current chat to config."
    ).raw { (c) ->
        configure { chat = c?.toString()?.takeIf { it.isNotBlank() } }
    },

    handler = NonHandle
)
