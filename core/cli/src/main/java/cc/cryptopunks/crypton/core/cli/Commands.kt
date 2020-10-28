package cc.cryptopunks.crypton.core.cli

import cc.cryptopunks.crypton.cli.command
import cc.cryptopunks.crypton.cli.commands
import cc.cryptopunks.crypton.cli.named
import cc.cryptopunks.crypton.cli.param
import cc.cryptopunks.crypton.cli.vararg
import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.context.Password
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.address
import cc.cryptopunks.crypton.context.inContext
import cc.cryptopunks.crypton.inContext

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
private const val PURGE = "purge"
private const val DEVICES = "devices"

private val navigateMain = MAIN to command { Route.Main }

private val navigateChat = CHAT to command(
    param()
) { (address) ->
    Route.Chat(account, address)
}


private val login = LOGIN to command(
    param()
) { (account) ->
    Exec.Connect.inContext(account)
}

private val addAccount = ADD to command(
    named(ACCOUNT),
    named(PASSWORD)
) { (account, password) ->
    Exec.Login(Account(address(account), Password(password)))
}

private val createAccount = CREATE to command(
    named(ACCOUNT),
    named(PASSWORD)
) { (account, password) ->
    Exec.Register(Account(address(account), Password(password)))
}

private val rosterItems = GET to mapOf(
    ITEMS to command {
        Get.RosterItems
    })

private val chat = CHAT to command(param()) { (address) ->
    Exec.CreateChat(Chat(address(address), address(account)))
}

private val sendMessage = SEND to mapOf(MESSAGE to command(vararg()) { message ->
    Exec.EnqueueMessage(message.joinToString(" "))
})

private val join = JOIN to command {
    (route as Route.Chat).run {
        Exec.JoinChat.inContext(account, address)
    }
}

private val listRooms = ROOMS to command(param()) { (account) ->
    Get.HostedRooms.inContext(account)
}

private val listJoinedRooms = JOINED to mapOf(ROOMS to command(param()) { (account) ->
    Get.JoinedRooms.inContext(account)
})

private val invite = INVITE to command(vararg()) { users ->
    (route as Route.Chat).run {
        Exec.Invite(users.map { address(it) }.toSet()).inContext(account, address)
    }
}

private val deleteChat = DELETE to command(vararg()) { chats ->
    Exec.DeleteChat.inContext(
        when (chats.isEmpty()) {
            true -> (route as Route.Chat).run { listOf(address.id) }
            false -> chats
        }
    )
}

private val getInfo = INFO to command(
    param(),
    param()
) { (account, chat) ->
    Get.ChatInfo.inContext(account, chat)
}

private val configure = CONFIGURE to command {
    Exec.ConfigureConference()
}

private val purgeDevices = PURGE to mapOf(DEVICES to command(param()) { (account) ->
    Exec.PurgeDeviceList.inContext(account)
})

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
    deleteChat,
    purgeDevices
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
