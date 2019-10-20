package cc.cryptopunks.crypton.smack.integration

import cc.cryptopunks.crypton.net.Net
import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.smack.SmackClientFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

abstract class ApiIntegrationTest :
    CoroutineScope by CoroutineScope(Dispatchers.Unconfined) {

    val createApi = SmackClientFactory()
    val baseId = Address(domain = "test.io")
    private val clients = mutableMapOf<Long, Account.Net>()
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
                try {
                    createAccount()
                    login()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
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

    fun client(index: Long): Net = synchronized(clients) {
        clients.getOrElse(index) {
            createApi(config(index)).let { it as Account.Net }.also {
                it.connect()
                clients[index] = it
            }
        } as Net
    }

    fun config(index: Long) = Net.Config(
        address = baseId.copy(local = "test$index"),
        password = "test$index"
    )
}

internal fun test(
    timeout: Long = 5000,
    block: suspend CoroutineScope.() -> Unit
) = runBlocking {
    withTimeout(timeout, block)
}