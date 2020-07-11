package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Password
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.Resource
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.Route
import kotlinx.coroutines.delay
import org.junit.Assert.assertEquals

suspend fun ClientDsl.prepare(
    address: Address,
    password: String
) {
    openSubscription()
    register(address, password)
}

suspend fun ClientDsl.tryRemoveAccount(
    address: Address,
    password: String
) {
    send(Account.Service.Add(Account(address, Password(password))))
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
    flush()
}

suspend fun ClientDsl.removeAccounts(vararg addresses: Address) {
    send(
        *addresses.map { address ->
            Account.Service.Remove(address, deviceOnly = false)
        }.toTypedArray()
    )
    flush()
    delay(1000)
}

suspend fun ClientDsl.register(
    address: Address,
    password: String
) {
    send(Account.Service.Register(Account(address, Password(password))))
    expect(
        Account.Service.Connecting(address),
        Account.Service.Connected(address)
    )
}

suspend fun ClientDsl.createChat(
    account: Address,
    chat: Address,
    users: List<Address> = listOf(chat)
) {
    send(Chat.Service.Create(Chat(chat, account, users)))
    expect(Chat.Service.ChatCreated(chat = chat))
}

suspend fun ClientDsl.openChat(
    account: Address,
    chat: Address
) {
    send(
        Route.Chat(account.id, chat.id),
        Chat.Service.SubscribeLastMessage(true)
    )
    flush()
}

suspend fun ClientDsl.sendMessage(
    message: String,
    account: Address,
    chat: Address
) {
    fun Message.requireStatus(
        status: Message.Status
    ) {
        assertEquals(toString(), chat, this.chat)
        assertEquals(toString(), message, text)
        assertEquals(toString(), Resource(account), from)
        assertEquals(toString(), Resource(chat), to)
        assertEquals(toString(), 0L, notifiedAt)
        assertEquals(toString(), 0L, readAt)
        assertEquals(toString(), status, this.status)
    }
    openSubscription()
    send(
        Chat.Service.EnqueueMessage(message)
    )

    waitFor<Chat.Service.Messages> {
        list.any { it.status == Message.Status.Sent }
    }.list.run {
        forEach { println(it) }
        first {
            it.status == Message.Status.Sent
        }.requireStatus(Message.Status.Sent)
    }
}

suspend fun ClientDsl.expectReceived(
    message: String,
    account: Address,
    chat: Address
) {
    expect(
        should<Chat.Service.Messages> {
            require(this.account == account) { this.account }
            require(list.size == 1) { list }
            list[0].run {
                require(text == message) { text }
                require(from.address == chat) { from.address }
                require(to.address == account) { to.address }
                require(this.chat == chat) { this.chat }
                require(notifiedAt == 0L) { notifiedAt }
                require(readAt == 0L) { readAt }
                require(status == Message.Status.Received) { status }
            }
            true
        }
    )
}


suspend fun ClientDsl.acceptSubscription(
    account: Address,
    subscriber: Address
) {
    send(Roster.Service.SubscribeItems(true, account))
    expect(
        should<Roster.Service.Items> {
            list.first { it.account == account }.run {
                require(this.account == account) { this.account }
                require(title == subscriber.id) { title }
                require(chatAddress == subscriber) { chatAddress }
                require(presence == Presence.Status.Subscribe) { presence }
                require(message == Message.Empty) { message }
                require(unreadMessagesCount == 0) { unreadMessagesCount }
            }
            true
        }
    ) { input ->
        (input as? Roster.Service.Items)?.run {
            println("Filtering items for acceptSubscription: acc = $account, sub = $subscriber")
            println(list.joinToString("\n"))
            list.any {
                it.account == account &&
                        it.chatAddress == subscriber &&
                        it.presence == Presence.Status.Subscribe
            }
        } ?: false
    }
    send(Roster.Service.AcceptSubscription(account, subscriber))
}

suspend fun ClientDsl.expectRosterItemMessage(text: String, account: Address, chat: Address) {
    expect(
        should<Roster.Service.Items> {
            log.d("Expect RosterItemMessage: $text, $account, $chat")
            list.firstOrNull { it.account == account && it.chatAddress == chat }?.run {
                require(title == chat.id) { title }
                require(presence == Presence.Status.Available) { presence }
                message.run {
                    assertEquals("\n" + list.joinToString("\n"), text, this.text)
                    require(this.chat == chat) { this.chat }
                    require(from.address == chat) { from.address }
                    require(to.address == account) { to.address }
                    require(status == Message.Status.Received)
                }
                require(unreadMessagesCount == 1) { unreadMessagesCount }
            } ?: require(false) {
                """
Expect RosterItemMessage: 
text = $text,
acc = $account 
chat = $chat 
But was:
""" + list.joinToString(separator = "\n")
            }
            true
        }
    ) { input ->
        (input as? Roster.Service.Items)?.run {
            list.any { it.account == account }
        } ?: false
    }
}
