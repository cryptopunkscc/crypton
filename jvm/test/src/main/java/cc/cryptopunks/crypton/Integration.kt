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
    openSubscription()
    log.d("Start client 1")
    tryRemoveAccount(address1, pass)
    register(address1, pass)
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
    expect(
        Chat.Service.Messages(
            account = address1,
            list = listOf(
                should {
                    require(text == "yo") { text }
                    require(from == Resource(address1)) { from }
                    require(to == Resource(address2)) { to }
                    require(chatAddress == address2) { chatAddress }
                    require(status == Message.Status.Queued) { status }
                    require(notifiedAt == 0L) { notifiedAt }
                    require(readAt == 0L) { readAt }
                    true
                }
            )
        ),
        Chat.Service.Messages(
            account = address1,
            list = listOf(
                should {
                    require(text == "yo") { text }
                    require(from == Resource(address1)) { from }
                    require(to == Resource(address2)) { to }
                    require(chatAddress == address2) { chatAddress }
                    require(status == Message.Status.Sending) { status }
                    require(notifiedAt == 0L) { notifiedAt }
                    require(readAt == 0L) { readAt }
                    true
                },
                should {
                    require(text == "yo") { text }
                    require(from == Resource(address1)) { from }
                    require(to == Resource(address2)) { to }
                    require(chatAddress == address2) { chatAddress }
                    require(status == Message.Status.Sent) { status }
                    require(notifiedAt == 0L) { notifiedAt }
                    require(readAt == 0L) { readAt }
                    true
                }
            )
        ),
        Chat.Service.Messages(
            account = address1,
            list = listOf(
                should {
                    require(text == "yo yo") { text }
                    require(from.address == address2) { from.address }
                    require(to.address == address1) { to.address }
                    require(chatAddress == address2) { chatAddress }
                    require(status == Message.Status.Received) { status }
                    require(notifiedAt == 0L) { notifiedAt }
                    require(readAt == 0L) { readAt }
                    true
                },
                ignore() // Fixme duplicated message
            )
        )
    )
    flush()
    delay(2000)
    log.d("Stop client 1")
}

suspend fun startClient2() = Client2.connectClient {
    openSubscription()
    log.d("Start client 2")
    tryRemoveAccount(address2, pass)
    register(address2, pass)
    send(
        Route.Roster,
        Roster.Service.SubscribeItems(true)
    )
    expect(
        ignore(),
        Roster.Service.Items(
            listOf(
                ignore(),
                should {
                    require(account == address2) { account }
                    require(title == address1.id) { title }
                    require(presence == Presence.Status.Subscribe) { presence }
                    require(message == Message.Empty) { message }
                    require(unreadMessagesCount == 0) { unreadMessagesCount }
                    true
                }
            )
        )
    )
    send(Roster.Service.AcceptSubscription(address2, address1))
    expect(
        ignore(),
        Roster.Service.Items(
            listOf(
                ignore(),
                should {
                    require(account == address2) { account }
                    require(title == address1.id) { title }
                    require(presence == Presence.Status.Available) { presence }
                    message.run {
                        require(text == "yo") { text }
                        require(chatAddress == address1) { chatAddress }
                        require(from.address == address1) { from.address }
                        require(to.address == address2 ) { to.address }
                        require(status == Message.Status.Received)
                    }
                    require(unreadMessagesCount == 1) { unreadMessagesCount }
                    true
                }
            )
        )
    )
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
    address: Address,
    password: String
) {
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
    address: Address,
    password: String
) {
    send(
        Route.Login,
        Account.Service.Register(Account(address, Password(password)))
    )
    expect(
        Account.Service.Connecting(address),
        Account.Service.Connected(address)
    )
}
