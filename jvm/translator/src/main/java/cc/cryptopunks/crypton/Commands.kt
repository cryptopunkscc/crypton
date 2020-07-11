package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Password
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.address

private val navigate = "navigate" to mapOf(
    "main" to command { Route.Main },
    "chat" to command(param()) { (address) ->
        Route.Chat(account, address)
    }
)

val COMMANDS: Map<Route<*>, Map<String, Any>> = mapOf(
    Route.Main to mapOf(
        "login" to command(
            param()
        ) { (account) ->
            Account.Service.Login(address(account))
        },
        "add" to command(
            named("account"),
            named("password")
        ) { (account, password) ->
            Account.Service.Add(Account(address(account), Password(password)))
        },
        "create" to command(
            named("account"),
            named("password")
        ) { (account, password) ->
            Account.Service.Register(Account(address(account), Password(password)))
        },
        "get" to mapOf(
            "items" to command {
                Roster.Service.GetItems
            }
        ),
        "chat" to mapOf(
            "to" to command(param()) { (address) ->
                Chat.Service.Create(Chat(address(address), address(account)))
            }
        )
    ),
    Route.Chat(isConference = false) to mapOf(
        "send" to mapOf(
            "message" to command(vararg()) { message ->
                Chat.Service.EnqueueMessage(message.joinToString(" "))
            }
        )
    ),
    Route.Chat(isConference = true) to mapOf(
        "send" to mapOf(
            "message" to command(vararg()) { message ->
                Chat.Service.EnqueueMessage(message.joinToString(" "))
            }
        ),
        "invite" to command(
            vararg()
        ) { users ->
            Chat.Service.Invite(users.map(::address))
        }
    )
).mapValues { (_, commands) -> commands + navigate }
