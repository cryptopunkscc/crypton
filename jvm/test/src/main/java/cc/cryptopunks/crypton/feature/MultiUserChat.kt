package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.Client1
import cc.cryptopunks.crypton.Client2
import cc.cryptopunks.crypton.Client3
import cc.cryptopunks.crypton.address1
import cc.cryptopunks.crypton.address2
import cc.cryptopunks.crypton.address3
import cc.cryptopunks.crypton.chatAddress
import cc.cryptopunks.crypton.connectClient
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.createChat
import cc.cryptopunks.crypton.openChat
import cc.cryptopunks.crypton.pass
import cc.cryptopunks.crypton.prepare
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun testMultiUserChat() {
    runBlocking {
        listOf(
            launch { client1() },
            launch { client2() },
            launch { client3() }
        ).joinAll()
        println("Finish multi user chat test")
    }
}

private suspend fun client1() = Client1.connectClient {
    log.d("Start client 1")
    prepare(address1, pass)

    createChat(address1, address2)
    openChat(address1, address2)

    send(Chat.Service.EnqueueMessage("yo 1-2"))
    flush()

    send(Route.Main)
    flush()

    createChat(address1, address3)
    openChat(address1, address3)

    send(Chat.Service.EnqueueMessage("yo 1-3"))
    flush()

    send(
        Route.Main,
        Roster.Service.SubscribeItems(true, address1)
    )

    waitFor<Roster.Service.Items> {
        list.filter { it.account == address1 }.run {
            isNotEmpty() && any {
                it.message.from.address == address1
                    && it.message.status == Message.Status.Sent
                    && it.chatAddress == address2
            } && any {
                it.message.from.address == address1
                    && it.message.status == Message.Status.Sent
                    && it.chatAddress == address3
            }
        }
    }

    waitFor<Roster.Service.Items> {
        list.any {
            it.chatAddress == chatAddress && it.presence == Presence.Status.Unavailable
        }
    }
    send(Roster.Service.AcceptSubscription(address1, chatAddress))
    flush()

    delay(1000)
    openChat(address1, chatAddress)
    send(Chat.Service.EnqueueMessage("yolo"))
    flush()

    delay(1000)
    log.d("Stop client 1")
}

private suspend fun client2() = Client2.connectClient {
    log.d("Start client 2")
    prepare(address2, pass)

    send(Roster.Service.SubscribeItems(true, address2))
    waitFor<Roster.Service.Items> {
        list.any {
            it.chatAddress == address1 && it.presence == Presence.Status.Subscribe
        }
    }

    send(Roster.Service.AcceptSubscription(address2, address1))
    waitFor<Roster.Service.Items> {
        list.any {
            it.chatAddress == address3 && it.presence == Presence.Status.Subscribe
        }
    }

    send(Roster.Service.AcceptSubscription(address2, address3))
    waitFor<Roster.Service.Items> {
        list.any {
            it.chatAddress == chatAddress && it.presence == Presence.Status.Unavailable
        }
    }

    send(Roster.Service.AcceptSubscription(address2, chatAddress))
    waitFor<Roster.Service.Items> {
        list.any {
            it.chatAddress == chatAddress
                && it.message.text == "yolo"
        }
    }

    delay(1000)
    log.d("Stop client 2")
}

private suspend fun client3() = Client3.connectClient {
    log.d("Start client 3")
    prepare(address3, pass)

    send(Roster.Service.SubscribeItems(true, address3))
    waitFor<Roster.Service.Items> {
        list.filter { it.account == address3 }.run {
            isNotEmpty() && any {
                it.chatAddress == address1
                    && it.presence == Presence.Status.Subscribe
            }
        }
    }

    send(Roster.Service.AcceptSubscription(address3, address1))
    flush()

    createChat(address3, address2)
    openChat(address3, address2)

    send(Chat.Service.EnqueueMessage("yo 3-2"))
    flush()

    send(
        Route.Main,
        Roster.Service.SubscribeItems(true, address3)
    )
    waitFor<Roster.Service.Items> {
        list.isNotEmpty() && list.any {
            it.chatAddress == address2
                && it.message.from.address == address3
                && it.message.status == Message.Status.Sent
        }
    }

    send(Chat.Service.CreateChat(address3, chatAddress, listOf(address1, address2)))
    send(Chat.Service.Create(Chat(chatAddress.toString(), chatAddress, address3, listOf(address1, address2))))
    waitFor<Roster.Service.Items> {
        list.any {
            it.chatAddress == chatAddress
                && it.message.text == "yolo"
        }
    }

    delay(1000)
    log.d("Stop client 3")
}
