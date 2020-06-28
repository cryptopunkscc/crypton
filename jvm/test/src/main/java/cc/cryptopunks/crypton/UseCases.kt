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

suspend fun ClientDsl.prepare(
    address: Address,
    password: String
) {
    openSubscription()
    tryRemoveAccount(address, password)
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
}

suspend fun ClientDsl.removeAccounts(vararg addresses: Address) {
    send(
        *addresses.map { address ->
            Account.Service.Remove(address, deviceOnly = false)
        }.toTypedArray()
    )
    flush()
    delay(200)
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
    chat: Address
) {
    send(Chat.Service.CreateChat(account, chat))
    expect(Chat.Service.ChatCreated(address = chat))
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
    chat: Address,
    subscribe: Boolean = false
) {
    fun Message.requireStatus(
        status: Message.Status
    ) {
        require(text == message) { text }
        require(from == Resource(account)) { from }
        require(to == Resource(chat)) { to }
        require(chatAddress == chat) { chatAddress }
        require(notifiedAt == 0L) { notifiedAt }
        require(readAt == 0L) { readAt }
        require(this.status == status) { this.status }
    }

    send(
        Chat.Service.EnqueueMessage(message)
    )
    if (subscribe) {
        expect(
            should<Chat.Service.Messages> {
                require(this.account == account) { this.account }
                require(list.size == 1) { list }
                list[0].requireStatus(Message.Status.Queued)
                true
            },
            should<Chat.Service.Messages> {
                require(this.account == account) { this.account }
                require(list.size == 2) { list }
                list[0].requireStatus(Message.Status.Sending)
                list[1].requireStatus(Message.Status.Sent)
                true
            }
        )
    } else expect(
        should<Chat.Service.Messages> {
            require(this.account == account) { this.account }
            require(list.size == 3) { list }
            list[0].requireStatus(Message.Status.Queued)
            list[1].requireStatus(Message.Status.Sending)
            list[2].requireStatus(Message.Status.Sent)
            true
        }
    )
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
                require(chatAddress == chat) { chatAddress }
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
    send(Roster.Service.SubscribeItems(true))
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
            list.any { it.account == account }
        } ?: false
    }
    send(Roster.Service.AcceptSubscription(account, subscriber))
}

suspend fun ClientDsl.expectRosterItemMessage(text: String, account: Address, chat: Address) {
    expect(
        should<Roster.Service.Items> {
            list.first { it.account == account }.run {
                require(this.account == account) { this.account }
                require(title == chat.id) { title }
                require(presence == Presence.Status.Available) { presence }
                message.run {
                    require(this.text == text) { this.text }
                    require(chatAddress == chat) { chatAddress }
                    require(from.address == chat) { from.address }
                    require(to.address == account) { to.address }
                    require(status == Message.Status.Received)
                }
                require(unreadMessagesCount == 1) { unreadMessagesCount }
            }
            true
        }
    ) { input ->
        (input as? Roster.Service.Items)?.run {
            list.any { it.account == account }
        } ?: false
    }
}
