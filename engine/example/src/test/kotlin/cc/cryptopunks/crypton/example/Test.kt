package cc.cryptopunks.crypton.example

import cc.cryptopunks.crypton.ConnectableBinding
import cc.cryptopunks.crypton.Context
import cc.cryptopunks.crypton.actor
import cc.cryptopunks.crypton.connectable
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.system.measureTimeMillis

class Test {

    @Test
    fun test() {
        measureTimeMillis {
            runBlocking {
                val job = Job()
                val received = mutableListOf<Any>()

                ConnectableBinding().apply {
                    +AppModule().connectable()
                    +actor { (input, _, out) ->
                        launch {
                            measureTimeMillis {
                                listOf(
                                    Entity.Command.Set(Entity("1")),
                                    Entity.Command.Set(Entity("2")),
                                    Entity.Command.Set(Entity("3")),
                                    Entity.Query.All,
                                    Context("1", Details.Query.Get)
                                ).forEach {
                                    out(it)
                                }
                            }.let {
                                println("actor sending $it ms")
                            }
                        }
                        launch {
                            measureTimeMillis {
                                input.take(2).toCollection(received)
                            }.let {
                                println("actor collecting $it ms")
                            }
                            job.complete()
                        }
                    }
                    job.join()
                }

                received.forEach(::println)
            }
        }.let {
            println("all $it ms")
        }
    }
}
