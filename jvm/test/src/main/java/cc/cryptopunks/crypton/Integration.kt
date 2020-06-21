package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Password
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    Log.init(JvmLog)
    TrustAllManager.install()
    runBlocking {
        launch {
            startServer()
        }.also {
            listOf(
                launch { startClient1() },
                launch { startClient2() }
            ).joinAll()
        }.cancel()
    }
}

object Client1
object Client2

private const val test1 = "test1"
private const val test2 = "test2"
private const val pass = "pass"
private const val domain = "janek-latitude"
private val address1 = Address(test1, domain)
private val address2 = Address(test2, domain)

suspend fun startClient1() = Client1.connectClient {
    openSubscription()
    log.d("Start client 1")
    tryRemoveAccount(test1, pass)
    register(test1, pass)
    send(
        Route.CreateChat().apply {
            accountId = "$test1@janek-latitude"
        },
        Chat.Service.CreateChat(address1, address2),
        Route.Chat().apply {
            accountId = "$test1@janek-latitude"
            chatAddress = "$test2@janek-latitude"
        },
        Chat.Service.SubscribeLastMessage(true)
    )
    send(Chat.Service.QueueMessage("yo"))
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
    tryRemoveAccount(test2, pass)
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
        Chat.Service.QueueMessage("yo yo")
    )
    flush()
    log.d("Stop client 2")
}

suspend fun ClientDsl.tryRemoveAccount(
    local: String,
    password: String
) {
    val address = Address(local, domain)
    send(
        Route.Login,
        Account.Service.Add(Account(address, Password(password)))
    )
    expect(
        Account.Service.Connecting(address),
        should<Account.Service.Status> {
            when (this) {
                is Account.Service.Error -> true
                is Account.Service.Connected -> true.also {
                    send(Account.Service.Remove(address, deviceOnly = false))
                }
                else -> false
            }
        }
    )
}


suspend fun ClientDsl.register(
    local: String,
    password: String
) {
    send(
        Route.Login,
        Account.Service.Register(Account(Address(local, domain), Password(password)))
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
    var command: Any = Account.Service.Add()
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

