package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.util.Log
import kotlinx.coroutines.*

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
                delay(2000)
                startClient2()
            }
        ).joinAll()
    }
}

private const val test1 = "test3"
private const val test2 = "test4"

suspend fun startClient1() = connectClient {
    openSubscription()
    log.d("Start client 1")
    send(
        Route.Login,
        Account.Service.Set(Account.Field.ServiceName, "janek-latitude"),
        Account.Service.Set(Account.Field.UserName, test1),
        Account.Service.Set(Account.Field.Password, "test"),
        Account.Service.Login
    )
    waitFor<Account.Service.Connected> {
        address.id == "$test1@janek-latitude"
    }
    send(
        Route.CreateChat().apply {
            accountId = "$test1@janek-latitude"
        },
        Chat.Service.CreateChat(Address.from("$test2@janek-latitude")),
        Route.Chat().apply {
            accountId = "$test1@janek-latitude"
            chatAddress = "$test2@janek-latitude"
        },
        Chat.Service.Subscribe.LastMessage
    )
    send(Chat.Service.SendMessage("yo"))
    waitFor<Chat.Service.Messages> {
        list.any { it.text == "yo yo" }
    }

    flush()
    delay(2000)
    log.d("Stop client 1")
}

suspend fun startClient2() = connectClient {
    openSubscription()
    log.d("Start client 2")
    send(
        Route.Login,
        Account.Service.Set(Account.Field.ServiceName, "janek-latitude"),
        Account.Service.Set(Account.Field.UserName, test2),
        Account.Service.Set(Account.Field.Password, "test"),
        Account.Service.Login
    )
    waitFor<Account.Service.Connected> {
        address.id == "$test2@janek-latitude"
    }
    send(Route.Roster)
    waitFor<Roster.Service.Items> {
        list.any { it.message.chatAddress.local == test1 }
    }
    send(
        Route.Chat().apply {
            accountId = "$test2@janek-latitude"
            chatAddress = "$test1@janek-latitude"
        },
        Chat.Service.Subscribe.LastMessage,
        Chat.Service.SendMessage("yo yo")
    )
    flush()
    log.d("Stop client 2")
}
