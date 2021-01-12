package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.cli.account
import cc.cryptopunks.crypton.cli.chat
import cc.cryptopunks.crypton.cliv2.Cli
import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.configure
import cc.cryptopunks.crypton.cliv2.named
import cc.cryptopunks.crypton.cliv2.param
import cc.cryptopunks.crypton.cliv2.raw
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.util.invoke

internal fun cliConfigure() = feature(
    command(
        named("account").copy(optional = true),
        named("chat").copy(optional = true),
        name = "configure cli"
    ).raw { args ->
        if (args.filterNotNull().isEmpty()) copy(
            result = Cli.Result.Return(config.toMap())
        ) else configure {
            args.let { (a, c) ->
                a { account = toString() }
                c { chat = toString() }
            }
        }
    }
)

internal fun cliSetAccount() = feature(
    command(
        param().copy(name = "account@domain.com", description = "Account address"),
        name = "a",
        description = "Set current account to config."
    ).raw { (a) ->
        configure { account = a?.toString()?.takeIf { it.isNotBlank() } }
    }
)

internal fun cliSetChat() = feature(
    command(
        param().copy(name = "chat@domain.com", description = "Chat address"),
        name = "c",
        description = "Set current chat to config."
    ).raw { (c) ->
        configure { chat = c?.toString()?.takeIf { it.isNotBlank() } }
    }
)
