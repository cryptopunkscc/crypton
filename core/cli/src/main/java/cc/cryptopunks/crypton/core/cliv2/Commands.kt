package cc.cryptopunks.crypton.core.cliv2

import cc.cryptopunks.crypton.cliv2.Cli
import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.commands
import cc.cryptopunks.crypton.cliv2.config
import cc.cryptopunks.crypton.cliv2.named
import cc.cryptopunks.crypton.cliv2.option
import cc.cryptopunks.crypton.cliv2.optional
import cc.cryptopunks.crypton.cliv2.param
import cc.cryptopunks.crypton.cliv2.raw
import cc.cryptopunks.crypton.cliv2.text
import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.context.Password
import cc.cryptopunks.crypton.context.Subscribe
import cc.cryptopunks.crypton.context.address
import cc.cryptopunks.crypton.inContext

private const val CHAT = "chat"
private const val LOGIN = "login"
private const val ADD = "add"
private const val CREATE = "create"
private const val ROSTER = "roster"
private const val GET = "get"
private const val SUBSCRIBE = "subscribe"
private const val SUBSCRIPTION = "subscription"
private const val ITEMS = "items"
private const val ACCOUNT = "account"
private const val ACCOUNTS = "accounts"
private const val PASSWORD = "password"
private const val SEND = "send"
private const val MESSAGE = "message"
private const val MESSAGES = "messages"
private const val INVITE = "invite"
private const val JOIN = "join"
private const val JOINED = "joined"
private const val HOSTED = "hosted"
private const val DELETE = "delete"
private const val INFO = "info"
private const val WITH = "with"
private const val CONFIG = "config"
private const val CONFIGURE = "configure"
private const val PURGE = "purge"
private const val DEVICES = "devices"
private const val ON = "on"
private const val EXEC = "exec"

private val login = command(
    param(),
) { (account) ->
    Exec.Connect.inContext(account)
}

private val getAccounts = command(
    description = "List all locally added accounts."
) {
    Get.Accounts
}

private val addAccount = command(
    named(ACCOUNT).copy(name = "local@domain", description = "XMPP account address"),
    named(PASSWORD).copy(name = "****", description = "XMPP account password"),
    description = "Add existing xmpp account to Crypton."
) { (account, password) ->
    Exec.Login(Account(address(account), Password(password)))
}

private val createAccount = command(
    named(ACCOUNT).copy(name = "local@domain", description = "XMPP account address"),
    named(PASSWORD).copy(name = "****", description = "XMPP account password"),
    description = "Create new xmpp account on server."
) { (account, password) ->
    Exec.Register(Account(address(account), Password(password)))
}

private val getRosterItems = command(
    description = "Get roster for all accounts."
) {
    Get.RosterItems
}

private val subscribeRosterItems = command(
    option("cancel").optional().copy(description = "Cancel subscription", value = false),
    description = "Subscribe roster for all accounts."
) { (cancel) ->
    Subscribe.RosterItems(!cancel.toBoolean(), list = false)
}

private val getSubscriptionStatus = command(
    config("account"),
    param().copy(name = "local@domain"),
    description = "Get roster subscription status"
) { (account, buddy) ->
    Get.SubscriptionStatus(address(buddy)).inContext(account)
}

private val deleteAccount = command(
    config("account"),
    description = "Delete account."
) { (account) ->
    Exec.RemoveAccount().inContext(account)
}

private val chatWith = command(
    config("account"),
    param().copy(name = "local@domain"),
    description = "Open chat with given address."
) { (account, address) ->
    Exec.CreateChat(Chat(address(address), address(account))).inContext(account)
}

private val invite = command(
    config("account"),
    config("chat"),
    text().copy(name = "local1@domain, local2@domain"),
    description = "Invite users to conference"
) { (account, chat, users) ->
    Exec.Invite(
        users.split(" ", ",").map { address(it) }.toSet()
    ).inContext(account, chat)
}

private val join = command(
    config("account"),
    config("chat"),
    description = "Accept buddy invitation or join to conference."
) { (account, chat) ->
    Exec.JoinChat.inContext(account, chat)
}

private val listJoinedRooms = command(
    config("account"),
    description = "List joined rooms."
) { (account) ->
    Get.JoinedRooms.inContext(account)
}

private val listHostedRooms = command(
    config("account"),
    description = "List hosted rooms."
) { (account) ->
    Get.HostedRooms.inContext(account)
}

private val chatInfo = command(
    config("account"),
    config("chat"),
    description = "Display info about chat."
) { (account, chat) ->
    Get.ChatInfo.inContext(account, chat)
}

private val deleteChat = command(
    config("account"),
    config("chat"),
    description = "Delete each chat form given list"
) { (account, chat) ->
    Exec.DeleteChat.inContext(account, chat)
}

private val configureConference = command(
    config("account"),
    config("chat"),
    description = "Configure conference (WIP)."
) { (account, chat) ->
    Exec.ConfigureConference().inContext(account, chat)
}

private val getMessages = command(
    config("account"),
    config("chat"),
    description = "Get roster for all accounts."
) { (account, chat) ->
    Get.Messages.inContext(account, chat)
}

private val subscribeMessages = command(
    config("account"),
    config("chat"),
    option("cancel").optional().copy(description = "Cancel subscription", value = false),
    description = "Subscribe roster for all accounts."
) { (account, chat, cancel) ->
    Subscribe.LastMessage(!cancel.toBoolean()).inContext(account, chat)
}

private val subscribeExecutionOnMessage = command(
    config("account"),
    config("chat"),
    param().copy(name = "command"),
    description = "Register subscription which will execute given command when any new message arrive in chat scope."
) { (account, chat, command) ->
    Subscribe.OnMessageExecute(command).inContext(account, chat)
}

private val sendMessage = command(
    config("account"),
    config("chat"),
    option("-!").optional().copy(description = "Send not encrypted message. Not recommended!"),
    text().copy(name = "message", description = "Message text"),
    description = "Send a message in chat."
) { (account, chat, notEncrypted, message) ->
    Exec.EnqueueMessage(message, notEncrypted.toBoolean().not()).inContext(account, chat)
}

private val message = command(
    config("account"),
    config("chat"),
    option("-!").optional().copy(description = "Send not encrypted message. Not recommended!"),
    text().copy(name = "message", description = "Message text"),
    description = "Send a message in chat."
) { (account, chat, notEncrypted, message) ->
    Exec.EnqueueMessage(message, notEncrypted.toBoolean().not()).inContext(account, chat)
}

private val devices = command(
    config("account"),
    description = "Purge device list for account"
) { (account) ->
    Exec.PurgeDeviceList.inContext(account)
}

private val config = command(
    named("account").copy(optional = true),
    named("chat").copy(optional = true)
).raw { args ->
    if (args.filterNotNull().isEmpty()) copy(
        result = Cli.Result.Return(config.toMap())
    ) else configure {
        args.getOrNull(0)?.run { account = toString() }
        args.getOrNull(1)?.run { chat = toString() }
    }
}

private val setAccount = command(
    param().copy(name = "account@domain.com", description = "Account address"),
    description = "Set current account to config."
).raw { (a) ->
    configure { account = a?.toString()?.takeIf { it.isNotBlank() } }
}

private val setChat = command(
    param().copy(name = "chat@domain.com", description = "Chat address"),
    description = "Set current chat to config."
).raw { (c) ->
    configure { chat = c?.toString()?.takeIf { it.isNotBlank() } }
}

val cryptonCommands = commands(
    ACCOUNTS to getAccounts,
    ADD to addAccount,
    CREATE to createAccount,
    DELETE to mapOf(ACCOUNT to deleteAccount),
    "-" to message,
    SEND to mapOf(MESSAGE to sendMessage),
    ROSTER to mapOf(
        ITEMS to mapOf(
            GET to getRosterItems,
            SUBSCRIBE to subscribeRosterItems
        ),
        SUBSCRIPTION to getSubscriptionStatus
    ),
    CHAT to mapOf(
        WITH to chatWith,
        INVITE to invite,
        JOIN to join,
        JOINED to listJoinedRooms,
        HOSTED to listHostedRooms,
        INFO to chatInfo,
        DELETE to deleteChat,
        CONFIGURE to configureConference,
        MESSAGES to commands(
            GET to getMessages,
            SUBSCRIBE to subscribeMessages
        ),
        ON to mapOf(MESSAGE to mapOf(EXEC to subscribeExecutionOnMessage))
    ),
    DEVICES to mapOf(PURGE to devices),
    CONFIG to config,
    "a" to setAccount,
    "c" to setChat
).copy(
    description = "Crypton CLI command list"
)
