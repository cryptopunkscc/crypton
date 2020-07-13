package cc.cryptopunks.crypton.json

import cc.cryptopunks.crypton.context.*
import org.junit.Assert.assertEquals
import org.junit.Test

class JsonTest {

    private val address = Address("test", "test.cc")
    private val password = Password("Test password")

    private val testObjects = listOf(
        TestClass(),
        Account(
            address = address,
            password = password
        ),
        Chat(
            title = "title",
            address = address,
            users = listOf(address)
        ),
        Route.Back,
        Route.Chat(
            account = Address.Empty,
            address = address("test@test.io")
        )
    )

    private val testJson = { any: Any ->
        assertEquals(any, any
            .formatJson()
            .also { println(it) }
            .parseJson(any::class)
            .also { println(it) })
    }

    @Test
    fun test() {
        testObjects.forEach(testJson)
    }
}

