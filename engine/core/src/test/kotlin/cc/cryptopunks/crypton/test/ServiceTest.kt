package cc.cryptopunks.crypton.test

import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.Async
import cc.cryptopunks.crypton.Handler
import cc.cryptopunks.crypton.Service
import cc.cryptopunks.crypton.Subscription
import cc.cryptopunks.crypton.service.connector
import cc.cryptopunks.crypton.factory.handler
import cc.cryptopunks.crypton.service.start
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.coroutines.EmptyCoroutineContext


private class SubscribeNumbers(override val enable: Boolean = true) : Subscription

private object ActionError : Action

private object AsyncError : Async

private object LongRunningAsync : Async

private object LongRunningAction : Action

private object ActionChannel2 : Action {
    override val channelId: Int get() = 2
}

private val actions = (0..100).map {
    object : Action {
        override fun toString(): String = "action: $it"
    }
}

private val async = (0..100).map {
    object : Async {
        override fun toString(): String = "async: $it"
    }
}



private val actionHandlers = actions.map {
    Handler(Handler.Key(it.javaClass)) { out, action: Action ->
        action.out()
    }
}

private val asyncHandlers = async.map {
    Handler(Handler.Key(it.javaClass)) { out, action: Async ->
        delay((0..1000L).random())
        action.out()
    }
}

private val subscribeNumbersHandler = handler { out, _: SubscribeNumbers ->
    var current = 0
    while (true) {
        current.out()
        current += 1
        delay(300)
    }
}

private val actionErrorHandler = handler { _, _: ActionError ->
    throw Exception("Action error")
}

private val asyncErrorHandler = handler { _, _: AsyncError ->
    delay(300)
    throw Exception("Async error")
}

private val longRunningAsyncHandler = handler { out, action: LongRunningAsync ->
    delay(10000000)
    action.out()
}

private val longRunningActionHandler = handler { out, action: LongRunningAction ->
    delay(3000)
    action.out()
}

private val actionChannel2Handler = handler { out, action: ActionChannel2 ->
    action.out()
}

private val handlers = EmptyCoroutineContext +
    actionHandlers.fold() +
    asyncHandlers.fold() +
    subscribeNumbersHandler +
    actionErrorHandler +
    asyncErrorHandler +
    longRunningAsyncHandler +
    longRunningActionHandler +
    actionChannel2Handler

class ServiceTest {

    @Test
    fun mixedTest() {

        val service = Service()
        val connector = service.connector()
        val results = mutableListOf<Any>()

        runBlocking(handlers) {
            CoroutineScope(
                coroutineContext +
                    newSingleThreadContext("test") +
                    SupervisorJob()
            ).run {

                val serviceJob = launch {
                    service.start()
                }

                val inputJob = launch {
                    connector.input.collect {
                        println("${System.nanoTime()} result: $it")
                        results.add(it)
                    }
                }

                flowOf(
                    LongRunningAction,
                    ActionChannel2,
                    actions[3],
                    actions[2],
                    actions[1],
                    LongRunningAsync,
                    SubscribeNumbers(),
                    async[0],
                    async[1],
                    async[2],
                    ActionError,
                    AsyncError,
                ).collect(
                    connector.output
                )

                delay(5000)
//                cancel()
                delay(500)
//                service.terminate()
                // Lines below are not required because scope is already cancelled
//                serviceJob.cancel()
//                inputJob.cancel()
            }
        }
    }

    @Test
    fun actionPerformance() {

    }
}
