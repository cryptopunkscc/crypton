package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Password
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.Resource
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.Subscribe
import cc.cryptopunks.crypton.context.context
import cc.cryptopunks.crypton.util.all
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
    send(Exec.Login(Account(address, Password(password))))
    expect(
        Account.Service.Connecting(address),
        should<Account.Service.Status> {
            when (this) {
                is Account.Service.Error -> true
                is Account.Service.Connected -> true.also {
                    send(Exec.RemoveAccount(deviceOnly = false).context(address))
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
            Exec.RemoveAccount(deviceOnly = false).context(address)
        }.toTypedArray()
    )
    flush()
    delay(1000)
}

suspend fun ClientDsl.register(
    address: Address,
    password: String
) {
    send(Exec.Register(Account(address, Password(password))))
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
    send(Exec.CreateChat(Chat(chat, account, users)).context(account))
    expect(Chat.Service.ChatCreated(chat = chat))
}

suspend fun ClientDsl.openChat(
    account: Address,
    chat: Address
) {
    send(
        Subscribe.LastMessage(true).context(account.id, chat.id)
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
        Exec.EnqueueMessage(message).context(account, chat)
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
    send(Subscribe.RosterItems(true, account))
    waitFor<Roster.Service.Items> {
        list.any {
            all(
                it.account == account,
                it.chatAddress == subscriber,
                it.presence == Presence.Status.Subscribe
            )
        }
    }
    send(Exec.JoinChat.context(account, subscriber))
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
