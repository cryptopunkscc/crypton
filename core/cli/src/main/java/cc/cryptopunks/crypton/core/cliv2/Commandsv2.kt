package cc.cryptopunks.crypton.core.cliv2

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.commands
import cc.cryptopunks.crypton.cliv2.named
import cc.cryptopunks.crypton.cliv2.param
import cc.cryptopunks.crypton.cliv2.text
import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.context.Password
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.address
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
    val account: String by config
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
    val account: String by config
    Exec.CreateChat(Chat(address(address), address(account)))
}

private val sendMessage = SEND to mapOf(MESSAGE to command(text()) { message ->
    Exec.EnqueueMessage(message.joinToString(" "))
})

private val join = JOIN to command {
    val account: String by config
    val chat: String by config
    Exec.JoinChat.inContext(account, chat)
}

private val listRooms = ROOMS to command(param()) { (account) ->
    Get.Rooms.inContext(account)
}

private val listJoinedRooms = JOINED to mapOf(ROOMS to command(param()) { (account) ->
    Get.JoinedRooms.inContext(account)
})

private val invite = INVITE to command(text()) { users ->
    val account: String by config
    val chat: String by config
    Exec.Invite(users.map { address(it) }.toSet()).inContext(account, chat)
}

private val deleteChat = DELETE to command(text()) { chats ->
    Exec.DeleteChat.inContext(
        when (chats.isEmpty()) {
            true -> listOf(config["chat"] as String)
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

val chatCommands = mapOf(
    sendMessage,
    join,
    invite,
    getInfo,
    configure
)

val cryptonCommands = commands(
    chatCommands + mainCommands
)
