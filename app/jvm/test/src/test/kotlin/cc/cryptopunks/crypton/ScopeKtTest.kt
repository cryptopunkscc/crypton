package cc.cryptopunks.crypton

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.plus
import org.junit.Test
import kotlin.system.measureNanoTime

class ScopeKtTest {

    @Test
    fun createServerScopeTest() {
        val config = RootScopeConfig(ServerConfig().default().local())
        var scope1: CoroutineScope

        measureNanoTime {
            scope1 = config.createServerScope()
        }.let(::println)

        measureNanoTime {
            scope1 + scope1.coroutineContext
        }.let(::println)

        measureNanoTime {
            scope1 + Job()
        }.let(::println)

        measureNanoTime {
            Dispatchers.Default + Job()
        }.let(::println)
    }
}
