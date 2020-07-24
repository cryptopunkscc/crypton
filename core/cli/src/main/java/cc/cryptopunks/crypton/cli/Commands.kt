package cc.cryptopunks.crypton.cli

import cc.cryptopunks.crypton.context
import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Password
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.address
import cc.cryptopunks.crypton.context.context
import cc.cryptopunks.crypton.translator.command
import cc.cryptopunks.crypton.translator.commands
import cc.cryptopunks.crypton.translator.named
import cc.cryptopunks.crypton.translator.param
import cc.cryptopunks.crypton.translator.vararg

private const val NAVIGATE = "navigate"
private const val MAIN = "main"
private const val CHAT = "chat"
private const val LOGIN = "login"
private const val ADD = "add"
private const val CREATE = "create"
private const val GET = "get"
private const val ITEMS = "items"
private const val ACCOUNT = "account"
private const val PASSWORD = "password"
private const val SEND = "send"
private const val MESSAGE = "message"
private const val INVITE = "invite"
private const val JOIN = "join"
private const val ROOMS = "rooms"
private const val JOINED = "joined"
private const val DELETE = "delete"
private const val INFO = "info"
private const val CONFIGURE = "configure"

private val navigateMain = MAIN to command { Route.Main }

private val navigateChat = CHAT to command(
    param()
) { (address) ->
    Route.Chat(account, address)
}


private val login = LOGIN to command(
    param()
) { (account) ->
    Account.Service.Login.context(address(account))
}

private val addAccount = ADD to command(
    named(ACCOUNT),
    named(PASSWORD)
) { (account, password) ->
    Account.Service.Add(Account(address(account), Password(password)))
}

private val createAccount = CREATE to command(
    named(ACCOUNT),
    named(PASSWORD)
) { (account, password) ->
    Account.Service.Register(Account(address(account), Password(password)))
}

private val rosterItems = GET to mapOf(
    ITEMS to command {
        Roster.Service.GetItems
    })

private val chat = CHAT to command(param()) { (address) ->
    Chat.Service.Create(Chat(address(address), address(account)))
}

private val sendMessage = SEND to mapOf(MESSAGE to command(vararg()) { message ->
    Chat.Service.EnqueueMessage(message.joinToString(" "))
})

private val join = JOIN to command {
    (route as Route.Chat).run {
        Roster.Service.Join.context(account, address)
    }
}

private val listRooms = ROOMS to command(param()) { (account) ->
    Chat.Service.ListRooms.context(address(account))
}

private val listJoinedRooms = JOINED to mapOf(ROOMS to command(param()) { (account) ->
    Chat.Service.ListJoinedRooms.context(address(account))
})

private val invite = INVITE to command(vararg()) { users ->
    (route as Route.Chat).run {
        Chat.Service.Invite(users.map { address(it) }.toSet()).context(account, address)
    }
}

private val deleteChat = DELETE to command(vararg()) { chats ->
    run {
        Chat.Service.DeleteChat.context(
            when (chats.isEmpty()) {
                true -> (route as Route.Chat).run { listOf(address.id) }
                false -> chats
            }
        )
    }
}

private val getInfo = INFO to command(
    param(),
    param()
) { (account, chat) ->
    Chat.Service.GetInfo.context(address(account), address(chat))
}

private val configure = CONFIGURE to command {
    Chat.Service.Configure()
}

private val navigate = NAVIGATE to mapOf(
    navigateMain,
    navigateChat
)

val mainCommands = mapOf(
    login,
    addAccount,
    createAccount,
    rosterItems,
    chat,
    listRooms,
    listJoinedRooms,
    deleteChat
)

val chatCommands = mainCommands + mapOf(
    sendMessage,
    join,
    invite,
    getInfo,
    configure
)

val cryptonCommands = commands(
    Route.Main to mainCommands,
    Route.Chat(isConference = false) to chatCommands,
    Route.Chat(isConference = true) to chatCommands
).mapValues { (_, commands) -> commands + navigate }
