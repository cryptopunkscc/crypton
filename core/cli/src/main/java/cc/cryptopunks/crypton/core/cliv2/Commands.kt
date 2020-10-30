package cc.cryptopunks.crypton.core.cliv2

import cc.cryptopunks.crypton.cliv2.Cli
import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.commands
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

private val login = LOGIN to command(
    param(),
) { (account) ->
    Exec.Connect.inContext(account)
}

private val accounts = ACCOUNTS to command(
    description = "List all locally added accounts."
) {
    Get.Accounts
}

private val addAccount = ADD to command(
    named(ACCOUNT).copy(name = "local@domain", description = "XMPP account address"),
    named(PASSWORD).copy(name = "****", description = "XMPP account password"),
    description = "Add existing xmpp account to Crypton."
) { (account, password) ->
    Exec.Login(Account(address(account), Password(password)))
}

private val createAccount = CREATE to command(
    named(ACCOUNT).copy(name = "local@domain", description = "XMPP account address"),
    named(PASSWORD).copy(name = "****", description = "XMPP account password"),
    description = "Create new xmpp account on server."
) { (account, password) ->
    Exec.Register(Account(address(account), Password(password)))
}


private val roster = ROSTER to mapOf(
    ITEMS to mapOf(
        GET to command(
            description = "Get roster for all accounts."
        ) {
            Get.RosterItems
        },
        SUBSCRIBE to command(
            option("cancel").optional().copy(description = "Cancel subscription", value = false),
            description = "Subscribe roster for all accounts."
        ) { (cancel) ->
            Subscribe.RosterItems(!cancel.toBoolean(), list = false)
        }
    )
)

private val chat = CHAT to mapOf(
    WITH to command(
        param().copy(name = "local@domain"),
        description = "Open chat with given address."
    ) { (address) ->
        val account: String by config
        Exec.CreateChat(Chat(address(address), address(account))).inContext(account)
    },
    INVITE to command(
        text().copy(name = "local1@domain, local2@domain"),
        description = "Invite users to conference"
    ) { users ->
        val account: String by config
        val chat: String by config
        Exec.Invite(users.map { address(it) }.toSet()).inContext(account, chat)
    },
    JOIN to command(
        description = "Accept buddy invitation or join to conference."
    ) {
        val account: String by config
        val chat: String by config
        Exec.JoinChat.inContext(account, chat)
    },
    JOINED to command(
        description = "List hosted rooms."
    ) {
        val account: String by config
        Get.JoinedRooms.inContext(account)
    },
    HOSTED to command(
        description = "List hosted rooms."
    ) {
        val account: String by config
        Get.HostedRooms.inContext(account)
    },
    INFO to command(
        description = "Display info about chat."
    ) {
        val account: String by config
        val chat: String by config
        Get.ChatInfo.inContext(account, chat)
    },
    DELETE to command(
//        text().copy(name = "local1@domain, local2@domain"),
        description = "Delete each chat form given list"
    ) { (chats) ->
        val chat: String by config
        Exec.DeleteChat.inContext(
            chat
//            when (chats.isEmpty()) {
//                true -> listOf(config["chat"] as String)
//                false -> chats.split(" ")
//            }
        )
    },
    CONFIGURE to command(
        description = "Configure conference (WIP)."
    ) {
        Exec.ConfigureConference()
    },
    MESSAGES to commands(
        GET to command(
            description = "Get roster for all accounts."
        ) {
            val account: String by config
            val chat: String by config
            Get.Messages.inContext(account, chat)
        },
        SUBSCRIBE to command(
            option("cancel").optional().copy(description = "Cancel subscription", value = false),
            description = "Subscribe roster for all accounts."
        ) { (cancel) ->
            val account: String by config
            val chat: String by config
            Subscribe.LastMessage(!cancel.toBoolean()).inContext(account, chat)
        }
    )
)

private val send = SEND to mapOf(
    MESSAGE to command(
        text().copy(name = "message", description = "Message text"),
        description = "Send a message in chat."
    ) { (message) ->
        val account: String by config
        val chat: String by config
        Exec.EnqueueMessage(message).inContext(account, chat)
    }
)

private val devices = DEVICES to mapOf(
    PURGE to command(
        description = "Purge device list for account"
    ) {
        val account: String by config
        Exec.PurgeDeviceList.inContext(account)
    }
)

private val config = CONFIG to command(
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

private val setAccount = "a" to command(
    param()
).raw { (a) ->
    configure { account = a?.toString()?.takeIf { it.isNotBlank() } }
}

private val setChat = "c" to command(
    param()
).raw { (c) ->
    configure { chat = c?.toString()?.takeIf { it.isNotBlank() } }
}

val cryptonCommands = commands(
    accounts,
//    login,
    addAccount,
    createAccount,
    send,
    roster,
    chat,
    devices,
    config,
    setAccount,
    setChat
).copy(
    description = "Crypton CLI command list"
)
