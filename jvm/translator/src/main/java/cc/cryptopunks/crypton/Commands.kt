package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Route

private val navigate = "navigate" to mapOf(
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
    Route.SetAccount to mapOf(
        "add" to command(
            named("account"),
            named("password")
        ) { (account, password) ->
            listOf(
                Account.Service.Set(Account.Field.UserName, account),
                Account.Service.Set(Account.Field.Password, password),
                Account.Service.Login()
            )
        },
        "create" to command(
            named("account"),
            named("password")
        ) { (account, password) ->
            listOf(
                Account.Service.Set(Account.Field.UserName, account),
                Account.Service.Set(Account.Field.Password, password),
                Account.Service.Register()
            )
        }
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
