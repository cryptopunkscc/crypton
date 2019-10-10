package cc.cryptopunks.crypton.smack.integration

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.smack.SmackClientFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

abstract class ApiIntegrationTest :
    CoroutineScope by CoroutineScope(Dispatchers.Unconfined) {

    val baseId = Address(domain = "test.io")
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
                createAccount()
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
        address = baseId.copy(local = "test$index"),
        password = "test$index"
    )
}

internal typealias createClient = SmackClientFactory

internal fun test(
    timeout: Long = 2000,
    block: suspend CoroutineScope.() -> Unit
) = runBlocking {
    withTimeout(timeout, block)
}