package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.Client1
import cc.cryptopunks.crypton.Client2
import cc.cryptopunks.crypton.acceptSubscription
import cc.cryptopunks.crypton.address1
import cc.cryptopunks.crypton.address2
import cc.cryptopunks.crypton.connectDslClient
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.createChat
import cc.cryptopunks.crypton.openChat
import cc.cryptopunks.crypton.pass
import cc.cryptopunks.crypton.prepare
import cc.cryptopunks.crypton.sendMessage
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

suspend fun testDirectMessaging() = coroutineScope {
    listOf(
        launch { client1() },
        launch { client2() }
    ).joinAll()
}

private suspend fun client1() = Client1.connectDslClient {
    expected.apply {
//        next<Exec.Register> {  }
//        next<Account.Connecting> {  }
//        next<Account.Connected> {  }
//        next<Chat.Create>(address1) {  }
//        next<Account.ChatCreated> {  }
//        next<Route.Chat> {  }
//        next<Chat.SubscribeLastMessage> {  }
//        next<Chat.EnqueueMessage> {  }
//        lazy<Chat.Messages> {
//            list.forEach { message ->
//                assertEquals(address2, message.chat)
//                assertEquals("yo", message.text)
//                assertEquals(Message.Status.Sent, message.status)
//            }
//        }
//        next<Chat.Messages> {
//            list.forEach { message ->
//                assertEquals(address2, message.chat)
//                assertEquals("yo yo", message.text)
//                assertEquals(Message.Status.Received, message.status)
//            }
//        }
    }

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
    waitFor<Chat.Messages> {
        list.any { it.text == "yo yo" }
    }
    delay(1000)
    printTraffic()
}

private suspend fun client2() = Client2.connectDslClient {
    prepare(address2, pass)
    acceptSubscription(address2, address1)
    delay(1000)
    waitFor<Roster.Items> {
        list.any { it.chatAddress == address1 && it.message.text == "yo" }
    }
    openChat(address2, address1)
    delay(1000)
    sendMessage("yo yo", address2, address1)
    delay(1000)
    printTraffic()
}
