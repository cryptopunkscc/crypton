package cc.cryptopunks.crypton.mux

import cc.cryptopunks.crypton.mux.channel.ArrayChannels
import cc.cryptopunks.crypton.mux.channel.factory
import cc.cryptopunks.crypton.mux.channel.getter
import cc.cryptopunks.crypton.mux.channel.next
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import kotlin.random.Random

class MultiplexerTest {

    @Test
    fun test(): Unit = runBlocking {
        val data = ByteArray(4096) { Random.nextInt().toByte() }
        val stream = Channel<ByteArray>()
        val channels = ArrayChannels()
        val createChannel = channels.factory()
        val getChannel = channels.getter()
        val (id, channel) = createChannel()
        val write = stream.byteWriter(id)
        var received = ByteArray(0)

        launch { stream.consumeAsFlow().demux(getChannel) }

        launch { channel.consumeAsFlow().collect { received += it } }
        write(data)

        while (received.size < data.size) delay(50)
        Assert.assertArrayEquals(data, received)

        channel.cancel()
        Assert.assertEquals(id, channels.ids.next())

        stream.close()
    }
}
