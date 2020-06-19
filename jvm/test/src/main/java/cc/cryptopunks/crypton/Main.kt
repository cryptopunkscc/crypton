package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.address
import cc.cryptopunks.crypton.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    Log.init(JvmLog)
    TrustAllManager.install()
    runBlocking {
        listOf(
            launch {
                startServer()
            },
            launch {
                delay(100)
                startClient1()
            },
            launch {
                delay(1000)
                startClient2()
            }
        ).joinAll()
    }
}

object Client1
object Client2

private const val test1 = "test3"
private const val test2 = "test4"
private const val pass = "pass"
private const val domain = "janek-latitude"
private val address1 = Address(test1, domain)
private val address2 = Address(test2, domain)

suspend fun startClient1() = Client1.connectClient {
    openSubscription()
    log.d("Start client 1")
    removeAccount(test1, pass)
    register(test1, pass)
    send(
        Route.CreateChat().apply {
            accountId = "$test1@janek-latitude"
        },
        Chat.Service.CreateChat(Address.from("$test2@janek-latitude")),
        Route.Chat().apply {
            accountId = "$test1@janek-latitude"
            chatAddress = "$test2@janek-latitude"
        },
        Chat.Service.SubscribeLastMessage(true)
    )
    send(Chat.Service.SendMessage("yo"))
    waitFor<Chat.Service.Messages> {
        list.any { it.text == "yo yo" }
    }

    flush()
    delay(2000)
    log.d("Stop client 1")
}

suspend fun startClient2() = Client2.connectClient {
    openSubscription()
    log.d("Start client 2")
    removeAccount(test2, pass)
    register(test2, pass)
    send(
        Route.Roster,
        Roster.Service.SubscribeItems(true)
    )
    waitFor<Roster.Service.Items> {
        list.any { it.presence == Presence.Status.Subscribe }
    }
    send(Roster.Service.AcceptSubscription(address2, address1))
    waitFor<Roster.Service.Items> {
        list.any { it.presence != Presence.Status.Subscribe }
    }
    send(
        Route.Chat().apply {
            accountId = "$test2@janek-latitude"
            chatAddress = "$test1@janek-latitude"
        },
        Chat.Service.SubscribeLastMessage(true),
        Chat.Service.SendMessage("yo yo")
    )
    flush()
    log.d("Stop client 2")
}

suspend fun ClientDsl.removeAccount(
    local: String,
    password: String
) {
    send(
        Route.Login,
        Account.Service.Set(Account.Field.ServiceName, "janek-latitude"),
        Account.Service.Set(Account.Field.UserName, local),
        Account.Service.Set(Account.Field.Password, password),
        Account.Service.Login()
    )
    val status = waitFor<Account.Service.Status> {
        (address.id == "$local@janek-latitude" && (this is Account.Service.Connected || this is Account.Service.Error))
    }
    if (status is Account.Service.Connected) {
        send(Account.Service.Remove(address("$local@$domain"), deviceOnly = false))
        delay(500)
    }
}


suspend fun ClientDsl.register(
    local: String,
    password: String
) {
    send(
        Route.Login,
        Account.Service.Set(Account.Field.ServiceName, "janek-latitude"),
        Account.Service.Set(Account.Field.UserName, local),
        Account.Service.Set(Account.Field.Password, password),
        Account.Service.Register()
    )
    waitFor<Account.Service.Connected> {
        address.id == "$local@janek-latitude"
    }
}


suspend fun ClientDsl.loginOrRegister(
    local: String,
    password: String
) {
    send(
        Route.Login,
        Account.Service.Set(Account.Field.ServiceName, "janek-latitude"),
        Account.Service.Set(Account.Field.UserName, local),
        Account.Service.Set(Account.Field.Password, password)
    )
    var command: Any = Account.Service.Login()
    send(command)
    while (true) {
        val status = waitFor<Account.Service.Status> {
            (address.id == "$local@janek-latitude" && (this is Account.Service.Connected || this is Account.Service.Error))
        }
        when (status) {
            is Account.Service.Error -> when {
                status.message?.contains("wait") == true -> {
                    delay(10000)
                    send(command)
                }
                status.message?.contains("not-authorized") == true -> {
                    command = Account.Service.Register()
                    send(command)
                }
            }
            is Account.Service.Connected -> return
            else -> Unit
        }
    }
}

