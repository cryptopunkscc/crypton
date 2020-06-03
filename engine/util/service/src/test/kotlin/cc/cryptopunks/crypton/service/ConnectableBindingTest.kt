package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Connector
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.Assert
import org.junit.Test
import kotlin.coroutines.CoroutineContext

class ConnectableBindingTest {

    @Test
    fun test(): Unit = runBlocking {
        // given
        val connectable = EchoConnectable()
        val channels = Channels()
        val binding = ConnectableBinding(channels)
        val output = mutableListOf<Any>()
        launch { channels.actor.openSubscription().consumeAsFlow().toCollection(output) }

        // when
        (1..2).asFlow().collect(binding.sendToService)
        binding + connectable
        (3..4).asFlow().collect(binding.sendToService)
        binding - connectable
        (5..6).asFlow().collect(binding.sendToService)
        binding + connectable

        // then
        delay(1)
        Assert.assertEquals(
            listOf(
                "0 1",
                "0 2",
                "0 3",
                "0 4",
                "0 cancel",
                "1 5",
                "1 6"
            ),
            output
        )
        binding.cancel()
    }

    class EchoConnectable : Connectable {
        var idCounter = 0
        override val coroutineContext: CoroutineContext =
            SupervisorJob() + newSingleThreadContext("")

        override fun Connector.connect(): Job = launch {
            val id = idCounter++
            input.map {
                "$id $it"
            }.onCompletion {
                output("$id cancel")
            }.collect(output)
        }
    }
}
