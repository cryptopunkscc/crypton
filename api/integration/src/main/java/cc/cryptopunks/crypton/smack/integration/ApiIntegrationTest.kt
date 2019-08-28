package cc.cryptopunks.crypton.smack.integration

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.api.entities.Jid
import cc.cryptopunks.crypton.smack.SmackClientFactory

abstract class ApiIntegrationTest {

    val baseJid = Jid(domain = "test.io")
    private val clients = mutableMapOf<Long, Client>()
    private var autoRemove = false

    val client1 get() = client(1)
    val client2 get() = client(2)
    val client3 get() = client(3)

    open fun init() {}

    open fun setUp() {
        init()
        autoRemove = clients.isNotEmpty()
        clients.values.forEach {
            with(it) {
                create()
                login()
            }
        }
    }

    open fun tearDown() {
        if (autoRemove) clients.values.forEach {
            with(it) {
                remove()
            }
        }
        clients.clear()
    }

    fun clients(range: IntRange) = range.map {
        client(it.toLong())
    }

    fun client(index: Long) = synchronized(clients) {
        clients.getOrElse(index) {
            createClient(config(index)).also {
                clients[index] = it
            }
        }
    }

    fun config(index: Long) = Client.Config(
        id = index,
        jid = baseJid.copy(local = "test$index"),
        password = "test$index"
    )
}

internal typealias createClient = SmackClientFactory