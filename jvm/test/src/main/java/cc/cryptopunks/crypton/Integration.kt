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
    main()
    openSubscription()
    log.d("Start client 1")
    tryRemoveAccount(address1, pass)
    register(address1, pass)
    send(
        Chat.Service.CreateChat(address1, address2),
        Route.Chat().apply {
            accountId = "$test1@janek-latitude"
            chatAddress = "$test2@janek-latitude"
        },
        Chat.Service.SubscribeLastMessage(true)
    )
    flush()
    // Wait for chat service initialization.
    // It is not necessary from technical point,
    // but in testing we want to receive all statuses [queued, sending, sent].
    // Without delay it will by only [sent].
    // With current arch design is hard to synchronize subscriptions with normal query/commands,
    // but it does not matter, from user perspective there is no use case for that.
    delay(1000)
    send(Chat.Service.EnqueueMessage("yo"))
    expect(
        should<Chat.Service.Messages> {
            require(account == address1) { account }
            require(list.size == 1) { list }
            list[0].run {
                require(text == "yo") { text }
                require(from == Resource(address1)) { from }
                require(to == Resource(address2)) { to }
                require(chatAddress == address2) { chatAddress }
                require(notifiedAt == 0L) { notifiedAt }
                require(readAt == 0L) { readAt }
                require(status == Message.Status.Queued) { status }
            }
            true
        },
        should<Chat.Service.Messages> {
            require(account == address1) { account }
            require(list.size == 2) { list }
            list[0].run {
                require(text == "yo") { text }
                require(from == Resource(address1)) { from }
                require(to == Resource(address2)) { to }
                require(chatAddress == address2) { chatAddress }
                require(notifiedAt == 0L) { notifiedAt }
                require(readAt == 0L) { readAt }
                require(status == Message.Status.Sending) { status }
            }
            list[1].run {
                require(text == "yo") { text }
                require(from == Resource(address1)) { from }
                require(to == Resource(address2)) { to }
                require(chatAddress == address2) { chatAddress }
                require(notifiedAt == 0L) { notifiedAt }
                require(readAt == 0L) { readAt }
                require(status == Message.Status.Sent) { status }
            }
            true
        },
        should<Chat.Service.Messages> {
            require(account == address1) { account }
            require(list.size == 1) { list }
            list[0].run {
                require(text == "yo yo") { text }
                require(from.address == address2) { from.address }
                require(to.address == address1) { to.address }
                require(chatAddress == address2) { chatAddress }
                require(notifiedAt == 0L) { notifiedAt }
                require(readAt == 0L) { readAt }
                require(status == Message.Status.Received) { status }
            }
            true
        }
    )
    flush()
    delay(5000) // wait for lazy errors
    log.d("Stop client 1")
}

suspend fun startClient2() = Client2.connectClient {
    main()
    openSubscription()
    log.d("Start client 2")
    tryRemoveAccount(address2, pass)
    register(address2, pass)
    send(Roster.Service.SubscribeItems(true))
    expect(
        ignore(),
        should<Roster.Service.Items> {
            require(list.size == 2) { list }
            list[1].run {
                require(account == address2) { account }
                require(title == address1.id) { title }
                require(presence == Presence.Status.Subscribe) { presence }
                require(message == Message.Empty) { message }
                require(unreadMessagesCount == 0) { unreadMessagesCount }
            }
            true
        }
    )
    send(Roster.Service.AcceptSubscription(address2, address1))
    expect(
        ignore(),
        should<Roster.Service.Items> {
            require(list.size == 2) { list }
            list[1].run {
                require(account == address2) { account }
                require(title == address1.id) { title }
                require(presence == Presence.Status.Available) { presence }
                message.run {
                    require(text == "yo") { text }
                    require(chatAddress == address1) { chatAddress }
                    require(from.address == address1) { from.address }
                    require(to.address == address2) { to.address }
                    require(status == Message.Status.Received)
                }
                require(unreadMessagesCount == 1) { unreadMessagesCount }
            }
            true
        }
    )
    send(
        Route.Chat().apply {
            accountId = "$test2@janek-latitude"
            chatAddress = "$test1@janek-latitude"
        },
        Chat.Service.EnqueueMessage("yo yo")
    )
    flush()
    delay(5000) // wait for lazy errors
    log.d("Stop client 2")
}

fun ClientDsl.main() {
    send(Route.Main)
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
