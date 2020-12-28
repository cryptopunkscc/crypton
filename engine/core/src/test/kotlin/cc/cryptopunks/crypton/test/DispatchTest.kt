package cc.cryptopunks.crypton.test

import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.Handler
import cc.cryptopunks.crypton.handle.dispatch
import cc.cryptopunks.crypton.service.start
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis

private val actions = (0..10000).map {
    object : Action {}
}

private val handlers = actions.map {
    echoHandler(it.javaClass)
}

private fun echoHandler(type: Class<*>) = Handler(
    key = Handler.Key(type),
    handle = { out, action: Action -> action.out() }
)

fun Iterable<CoroutineContext>.fold() = reduce { acc, element -> acc + element }

class ServiceKtTest {

    @Test
    fun simplePerformanceTest() {
        runBlocking(handlers.fold()) {

            flowOf(actions[1000]).run {
                measureTimeMillis {
                    start { }
                }.let(::println)
            }

            flowOf(actions[1000]).run {
                measureTimeMillis {
                    start { }
                }.let(::println)
            }

            flowOf(actions[0]).run {
                measureTimeMillis {
                    start {  }
                }.let(::println)
            }

            actions.asFlow().run {
                measureTimeMillis {
                    start {  }
                }.let(::println)
            }

            actions.map { actions[0] }.asFlow().run {
                measureTimeMillis {
                    start {}
                }.let(::println)
            }

            actions.map { actions.random() }.asFlow().run {
                measureTimeMillis {
                    start {  }
                }.let(::println)
            }
        }
    }
}

