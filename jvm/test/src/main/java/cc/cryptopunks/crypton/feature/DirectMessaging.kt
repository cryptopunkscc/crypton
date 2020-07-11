package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.Client1
import cc.cryptopunks.crypton.Client2
import cc.cryptopunks.crypton.acceptSubscription
import cc.cryptopunks.crypton.address1
import cc.cryptopunks.crypton.address2
import cc.cryptopunks.crypton.connectClient
import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.createChat
import cc.cryptopunks.crypton.expectReceived
import cc.cryptopunks.crypton.openChat
import cc.cryptopunks.crypton.pass
import cc.cryptopunks.crypton.prepare
import cc.cryptopunks.crypton.sendMessage
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals

suspend fun testDirectMessaging() = coroutineScope {
    listOf(
        launch { client1() },
        launch { client2() }
    ).joinAll()
}

private suspend fun client1() = Client1.connectClient {
    expected.apply {
        next<Account.Service.Register> {  }
        next<Account.Service.Connecting> {  }
        next<Account.Service.Connected> {  }
        next<Chat.Service.CreateChat> {  }
        next<Chat.Service.ChatCreated> {  }
        next<Route.Chat> {  }
        next<Chat.Service.SubscribeLastMessage> {  }
        next<Chat.Service.EnqueueMessage> {  }
        lazy<Chat.Service.Messages> {
            list.forEach { message ->
                assertEquals(address2, message.chat)
                assertEquals("yo", message.text)
                assertEquals(Message.Status.Sent, message.status)
            }
        }
        next<Chat.Service.Messages> {
            list.forEach { message ->
                assertEquals(address2, message.chat)
                assertEquals("yo yo", message.text)
                assertEquals(Message.Status.Received, message.status)
            }
        }
    }

    log.d("Start client 1")
    prepare(address1, pass)
    createChat(address1, address2)
    openChat(address1, address2)
    // Wait for chat service initialization.
    // It is not necessary from technical point,
    // but in testing we want to receive all statuses [queued, sending, sent].
    // Without delay it will by only [sent].
    // With current arch design is hard to synchronize subscriptions with normal query/commands,
    // but it does not matter, from user perspective there is no use case for that.
    delay(1000)
    sendMessage("yo", address1, address2)
    waitFor<Chat.Service.Messages> {
        list.any { it.text == "yo yo" }
    }
    log.d("Stop client 1")
    delay(1000)
    printTraffic()
}

private suspend fun client2() = Client2.connectClient {
    log.d("Start client 2")
    prepare(address2, pass)
    acceptSubscription(address2, address1)
    delay(1000)
    waitFor<Roster.Service.Items> {
        list.any { it.chatAddress == address1 && it.message.text == "yo" }
    }
    openChat(address2, address1)
    delay(1000)
    sendMessage("yo yo", address2, address1)
    log.d("Stop client 2")
    delay(1000)
    printTraffic()
}
