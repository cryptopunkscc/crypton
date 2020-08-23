package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.translator.Check
import cc.cryptopunks.crypton.translator.Context
import cc.cryptopunks.crypton.translator.translateCli
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class BusTest {

    @Test
    fun `test root command`() {
        runBlocking {
            assertEquals(
                listOf(listOf("0", "1")),
                listOf("cmd0 0 1")
                    .asFlow()
                    .translateCli(Context(TEST_COMMANDS))
                    .mapNotNull { it.result }
                    .toList()
            )
        }
    }

    @Test
    fun `test sub command`() {
        runBlocking {
            assertEquals(
                listOf(
                    Check.Suggest(listOf("subCmd0")),
                    Check.Suggest(listOf("arg0", "arg1")),
                    Check.Suggest(listOf("arg0")),
                    Check.Suggest(listOf("arg0")),
                    listOf("0", "1")
                ),
                listOf("cmd1", "subCmd0", "arg1 1", "arg0", "0")
                    .asFlow()
                    .translateCli(
                        Context(
                            TEST_COMMANDS
                        )
                    )
                    .mapNotNull { it.result }
                    .toList()
            )
        }
    }

    @Test
    fun `malformed input`() {
        runBlocking {
            assertTrue(
                listOf("uknown", "comand", "foo", "bar")
                    .asFlow()
                    .translateCli(
                        Context(
                            TEST_COMMANDS
                        )
                    )
                    .mapNotNull { it.result }
                    .onEach { println(it) }
                    .toList()
                    .all { it is Check.Suggest }
            )
        }
    }
}
