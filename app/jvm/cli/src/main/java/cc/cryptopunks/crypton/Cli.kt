package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.net.connectClientSocket
import cc.cryptopunks.crypton.net.connector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import java.net.InetSocketAddress

fun main(args: Array<String> = emptyArray()) {
    runBlocking {

//        initJvmLog()

        CliClient(
            if (args.isEmpty()) systemInput()
            else flowOf(args.joinToString(" "))
        ).run {
            connectClientSocket(
                InetSocketAddress("127.0.0.1", 2323),
                Dispatchers.IO
            )
                .connector()
                .logging()
                .connect()
                .join()
        }
    }
}
