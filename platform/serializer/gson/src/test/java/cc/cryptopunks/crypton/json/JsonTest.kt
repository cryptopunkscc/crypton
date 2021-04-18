package cc.cryptopunks.crypton.json

import cc.cryptopunks.crypton.gson.formatJson
import cc.cryptopunks.crypton.gson.parseJson
import org.junit.Assert.assertEquals
import org.junit.Test

class JsonTest {

    private val testObjects = listOf(
        TestClass()
    )

    private val testJson = { any: Any ->
        assertEquals(any, any
            .formatJson()
            .also { println(it) }
            .parseJson(any::class.java)
            .also { println(it) })
    }

    @Test
    fun test() {
        testObjects.forEach(testJson)
    }
}

