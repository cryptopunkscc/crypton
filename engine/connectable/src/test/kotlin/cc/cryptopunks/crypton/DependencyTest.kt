package cc.cryptopunks.crypton

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Assert.*
import org.junit.Test
import kotlin.system.measureNanoTime

private object TestObj1
private object TestObj2
private object TestObj3

private val CoroutineScope.test1 by dep<TestObj1>()

private val context = TestObj1.asDep() + TestObj2.asDep() + TestObj3.asDep()

class DependencyTest {

    @Test
    fun test1() {
        runBlocking(context) {
            assertEquals(
                TestObj1,
                test1
            )
        }
    }

    @Test
    fun test2() {
        val map = mapOf(
            TestObj2 to TestObj1,
            TestObj3 to TestObj2
        )
        runBlocking {
            withContext(TestObj1.asDep()
                + TestObj2.asDep()
                + TestObj3.asDep()) {

                var a: Any? = null
                var b: Any? = null

                println(
                    measureNanoTime {
                        a = test1
                    }
                )

                println(
                    measureNanoTime {
                        b = map[TestObj2]
                    }
                )
            }
        }
    }
}
