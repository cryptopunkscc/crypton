package cc.cryptopunks.crypton

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot

inline fun <reified T : Any> ignore(): T = mockk(relaxed = true) {
    val slot = slot<Any>()
    every { this@mockk == capture(slot) } answers {
        println("${this@mockk} match ignored: ${slot.captured}")
        true
    }
}

inline fun <reified T : Any> should(crossinline match: T.() -> Boolean): T = mockk(relaxed = true) {
    val slot = slot<T>()
    var matched = false
    every { this@mockk == capture(slot) } answers {
        val value = slot.captured
        value.match().also { matches ->
            matched = matches
            if (matches)
                println("${this@mockk} match succeed: $value") else
                println("${this@mockk} match failed: $value")
        }
    }
    every { this@mockk.toString() } answers {
        if (matched && slot.isCaptured)
            slot.captured.toString() else
            callOriginal()
    }
}
