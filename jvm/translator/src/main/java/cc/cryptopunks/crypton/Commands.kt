package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Password
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.address

private val navigate = "navigate" to mapOf(
    "accounts" to command { Route.Login },
    "roster" to command { Route.Roster },
    "create" to mapOf(
        "chat" to command {
            Route.CreateChat().apply {
                accountId = account
            }
        }
    ),
    "chat" to command(param()) { (address) ->
        Route.Chat().apply {
            accountId = account
            chatAddress = address
        }
    }
)

val COMMANDS: Map<Route, Map<String, Any>> = mapOf(
    Route.Login to mapOf(
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
        }
    ),
    Route.Roster to mapOf(
        "get" to mapOf(
            "items" to command {
                Roster.Service.GetItems
            }
        )
    ),
    Route.Chat() to mapOf(
        "send" to mapOf(
            "message" to command(vararg()) { message ->
                Chat.Service.SendMessage(message.joinToString(" "))
            }
        )
    ),
    Route.CreateChat() to (mapOf(
        "chat" to mapOf(
            "to" to command(param()) { (address) ->
                Chat.Service.CreateChat(Address.from(address))
            }
        )
    ))
).mapValues { (_, commands) -> commands + navigate }

fun param() = Input.Simple()
fun vararg() = Input.Vararg()
fun named(name: String) = Input.Named(name)
